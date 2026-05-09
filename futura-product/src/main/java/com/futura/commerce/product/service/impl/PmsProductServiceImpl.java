package com.futura.commerce.product.service.impl;

import com.futura.commerce.product.dto.IsPromotionDTO;
import com.futura.commerce.product.dto.PmsPromotionSearchDTO;
import com.futura.commerce.product.dto.PmsPromotionVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.futura.commerce.mbg.model.PmsProductFeature;
import com.futura.commerce.mbg.model.ProductParams;
import com.futura.commerce.product.dto.ProductDetailDTO;
import com.futura.commerce.product.service.CommonImageService;
import com.futura.commerce.product.service.PmsProductCategoryService;
import com.futura.commerce.product.service.PmsProductFeatureService;
import com.futura.commerce.product.service.PmsProductService;
import com.futura.commerce.product.service.ProductParamsService;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
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

    @Resource
    private PmsProductFeatureService pmsProductFeatureService;

    @Resource
    private ProductParamsService productParamsService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ObjectMapper objectMapper;

    private static final String USER_DETAIL_KEY = "product:detail:";

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
    public CommonResult<String> updateStatus(Long id, Integer status) {
        if (id == null) {
            return CommonResult.failed("Invalid product ID");
        }
        Optional<PmsProduct> productOpt = pmsProductRepository.findById(id);
        if (productOpt.isEmpty()) {
            return CommonResult.failed("Product not found");
        }
        if (status == null || (status != 0 && status != 1)) {
            return CommonResult.failed("Status must be 0 or 1");
        }
        PmsProduct product = productOpt.get();
        product.setPublishStatus(status);
        pmsProductRepository.save(product);

        return CommonResult.success(status == 1 ? "Product published successfully" : "Product unpublished successfully");
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

    @Override
    public CommonResult<ProductDetailDTO> detail(Long productId) {
        if (productId == null) {
            return CommonResult.failed("Invalid product ID");
        }

        String cacheKey = USER_DETAIL_KEY + productId;
        try {
            String cached = stringRedisTemplate.opsForValue().get(cacheKey);
            if (cached != null) {
                if (cached.trim().isEmpty()) {
                    return CommonResult.failed("Product does not exist");
                }
                log.info("Cache hit for product detail: {}", productId);
                ProductDetailDTO cachedDto = objectMapper.readValue(cached, ProductDetailDTO.class);
                return CommonResult.success(cachedDto, "Product detail retrieved successfully");
            }
        } catch (Exception e) {
            log.warn("Failed to retrieve product detail from cache: {}", e.getMessage());
        }

        // Prevent cache penetration
        Optional<PmsProduct> productOpt = pmsProductRepository.findById(productId);
        if (productOpt.isEmpty()) {
            try {
                stringRedisTemplate.opsForValue().set(cacheKey, " ", 1, TimeUnit.MINUTES);
            } catch (Exception e) {
                log.warn("Failed to write empty product to cache: {}", e.getMessage());
            }
            return CommonResult.failed("Product does not exist");
        }

        List<PmsProductFeature> featureList = pmsProductFeatureService.findByProductId(productId);
        List<ProductParams> paramsList = productParamsService.findByProductsId(productId);

        if (featureList == null) featureList = new ArrayList<>();
        if (paramsList == null) paramsList = new ArrayList<>();

        ProductDetailDTO detailDTO = new ProductDetailDTO();
        detailDTO.setParamsList(paramsList);
        detailDTO.setFeatureList(featureList);

        try {
            String json = objectMapper.writeValueAsString(detailDTO);
            stringRedisTemplate.opsForValue().set(cacheKey, json, 1, TimeUnit.HOURS);
        } catch (Exception e) {
            log.warn("Failed to write product detail to cache: {}", e.getMessage());
        }

        log.info("Cache miss for product detail: {}", productId);
        return CommonResult.success(detailDTO, "Product detail retrieved successfully");
    }
}
