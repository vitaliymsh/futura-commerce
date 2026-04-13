package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.dto.IsPromotionDTO;
import com.futura.commerce.admin.dto.PmsPromotionSearchDTO;
import com.futura.commerce.admin.dto.PmsPromotionVO;
import com.futura.commerce.admin.service.CommonImageService;
import com.futura.commerce.admin.service.PmsProductCategoryService;
import com.futura.commerce.admin.service.PmsProductService;
import com.futura.commerce.common.baseCommon.CommonResult;
import com.futura.commerce.mbg.model.PmsProduct;
import com.futura.commerce.mbg.model.PmsProductCategory;
import com.futura.commerce.mbg.repository.PmsProductRepository;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for managing PmsProduct
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class PmsProductServiceImpl implements PmsProductService {

    @Resource
    private PmsProductRepository pmsProductRepository;

    @Lazy
    @Resource
    private PmsProductCategoryService pmsProductCategoryService;

    @Resource
    private CommonImageService commonImageService;

    @Override
    public List<PmsProduct> findAll() {
        return pmsProductRepository.findAll();
    }

    @Override
    public Optional<PmsProduct> findById(Long id) {
        return pmsProductRepository.findById(id);
    }

    @Override
    public PmsProduct save(PmsProduct entity) {
        return pmsProductRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        pmsProductRepository.deleteById(id);
    }

    @Override
    public CommonResult<String> isOpen(IsPromotionDTO promotionVO) {
        if (promotionVO == null || promotionVO.getProductId() == null) {
            return CommonResult.failed("Invalid product promotion parameter");
        }

        Optional<PmsProduct> productOpt = pmsProductRepository.findById(promotionVO.getProductId());
        if (productOpt.isEmpty()) {
            return CommonResult.failed("Product not found");
        }

        PmsProduct product = productOpt.get();
        product.setIsPromotion(promotionVO.getIsPromotion());
        pmsProductRepository.save(product);

        return Integer.valueOf(1).equals(promotionVO.getIsPromotion())
                ? CommonResult.success("Product promotion enabled")
                : CommonResult.success("Product promotion disabled");
    }

    @Override
    public CommonResult<List<PmsPromotionVO>> goodsList() {
        CommonResult<Page<PmsPromotionVO>> pageResult = goodsPagination(1, Integer.MAX_VALUE);
        List<PmsPromotionVO> list = pageResult.getData() != null ? pageResult.getData().getContent() : List.of();
        return CommonResult.success(list, "Query successful");
    }

    @Override
    public CommonResult<Page<PmsPromotionVO>> goodsPagination(Integer page, Integer pageSize) {
        if (page == null || page < 1) page = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;

        Pageable pageable = PageRequest.of(page - 1, Math.min(pageSize, 1000));
        Page<PmsProduct> productPage = pmsProductRepository.findAll(pageable);

        if (productPage == null || productPage.isEmpty()) {
            return CommonResult.success(null, "No products found");
        }

        List<PmsPromotionVO> voList = productPage.getContent().stream().map(pmsProduct -> {
            PmsPromotionVO promotionVO = new PmsPromotionVO();
            promotionVO.setId(pmsProduct.getId());
            promotionVO.setName(pmsProduct.getName());
            Long categoryId = pmsProduct.getCategoryId();
            if (categoryId != null) {
                Optional<PmsProductCategory> categoryOpt = pmsProductCategoryService.findById(categoryId);
                categoryOpt.ifPresent(c -> promotionVO.setCategoryName(c.getName()));
            }
            promotionVO.setCategoryId(categoryId);
            promotionVO.setPublishStatus(pmsProduct.getPublishStatus());
            promotionVO.setPrice(pmsProduct.getPrice());
            promotionVO.setStock(pmsProduct.getStock());
            promotionVO.setPic(pmsProduct.getPic());
            return promotionVO;
        }).collect(Collectors.toList());

        Page<PmsPromotionVO> voPage = new PageImpl<>(voList, pageable, productPage.getTotalElements());
        return CommonResult.success(voPage, "Products list queried successfully");
    }

    @Override
    public CommonResult<Page<PmsProduct>> getPromotionByKeySearch(Integer page,
                                                                   Integer pageSize,
                                                                   Integer status,
                                                                   String keySearch,
                                                                   Integer categoryId) {
        if (page == null || page < 1) page = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;

        Pageable pageable = PageRequest.of(page - 1, pageSize);

        Specification<PmsProduct> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("publishStatus"), status));
            }
            if (StringUtils.hasText(keySearch)) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + keySearch.trim() + "%"));
            }
            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("categoryId"), categoryId.longValue()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<PmsProduct> pageResult = pmsProductRepository.findAll(spec, pageable);
        return CommonResult.success(pageResult, "Query successful");
    }

    @Override
    public CommonResult<PmsPromotionSearchDTO> getPromotionSave(PmsPromotionSearchDTO promotion) {
        if (promotion == null) {
            return CommonResult.failed("Promotion payload cannot be null");
        }

        PmsProduct product;
        if (promotion.getId() != null) {
            Optional<PmsProduct> productOpt = pmsProductRepository.findById(promotion.getId());
            if (productOpt.isEmpty()) {
                return CommonResult.failed("Product not found: " + promotion.getId());
            }
            product = productOpt.get();
        } else {
            product = new PmsProduct();
        }

        product.setName(promotion.getName());
        product.setCategoryId(promotion.getCategoryId());
        product.setPrice(promotion.getPrice());
        product.setStock(promotion.getStock());
        product.setPic(promotion.getPic());
        product.setPublishStatus(promotion.getPublishStatus());
        product.setDescription(promotion.getDescription());

        PmsProduct saved = pmsProductRepository.save(product);
        promotion.setId(saved.getId());
        return CommonResult.success(promotion, "Product saved successfully");
    }

    @Override
    public CommonResult<String> upload(MultipartFile file) {
        return commonImageService.upload(file);
    }

    @Override
    public CommonResult<String> delete(Long id) {
        if (id == null) {
            return CommonResult.failed("Invalid product ID");
        }
        Optional<PmsProduct> productOpt = pmsProductRepository.findById(id);
        if (productOpt.isEmpty()) {
            return CommonResult.failed("Product not found");
        }
        pmsProductRepository.deleteById(id);
        return CommonResult.success("Product deleted successfully");
    }

    @Override
    public CommonResult<String> updateStatus(Long id) {
        if (id == null) {
            return CommonResult.failed("Invalid product ID");
        }
        Optional<PmsProduct> productOpt = pmsProductRepository.findById(id);
        if (productOpt.isEmpty()) {
            return CommonResult.failed("Product not found");
        }
        PmsProduct product = productOpt.get();
        Integer currentStatus = product.getPublishStatus();
        int newStatus = (currentStatus != null && currentStatus == 1) ? 0 : 1;
        product.setPublishStatus(newStatus);
        pmsProductRepository.save(product);

        return CommonResult.success(newStatus == 1 ? "Product published successfully" : "Product unpublished successfully");
    }

    @Override
    public CommonResult<String> updateProduct(Long id, PmsPromotionSearchDTO promotion) {
        if (id == null) {
            return CommonResult.failed("Invalid product ID");
        }
        Optional<PmsProduct> productOpt = pmsProductRepository.findById(id);
        if (productOpt.isEmpty()) {
            return CommonResult.failed("Product not found");
        }

        PmsProduct product = productOpt.get();
        if (promotion != null) {
            if (promotion.getName() != null) product.setName(promotion.getName());
            if (promotion.getCategoryId() != null) product.setCategoryId(promotion.getCategoryId());
            if (promotion.getPrice() != null) product.setPrice(promotion.getPrice());
            if (promotion.getStock() != null) product.setStock(promotion.getStock());
            if (promotion.getPic() != null) product.setPic(promotion.getPic());
            if (promotion.getPublishStatus() != null) product.setPublishStatus(promotion.getPublishStatus());
            if (promotion.getDescription() != null) product.setDescription(promotion.getDescription());
        }

        pmsProductRepository.save(product);
        return CommonResult.success("Product updated successfully");
    }
}
