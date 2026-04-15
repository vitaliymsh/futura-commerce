package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.dto.PmsSkuSearchDTO;
import com.futura.commerce.admin.service.CommonImageService;
import com.futura.commerce.admin.service.PmsProductService;
import com.futura.commerce.admin.service.PmsProductSkuService;
import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.PmsProduct;
import com.futura.commerce.mbg.model.PmsProductSku;
import com.futura.commerce.mbg.repository.PmsProductRepository;
import com.futura.commerce.mbg.repository.PmsProductSkuRepository;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Product SKU service implementation
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class PmsProductSkuServiceImpl implements PmsProductSkuService {

    @Resource
    private PmsProductSkuRepository pmsProductSkuRepository;

    @Resource
    private PmsProductRepository pmsProductRepository;

    @Resource
    private PmsProductService pmsProductService;

    @Resource
    private CommonImageService commonImageService;

    @Override
    public CommonResult<String> updateSku(Long id, List<PmsProductSku> pmsProductSkuList) {
        return CommonResult.success("");
    }

    @Override
    public CommonResult<List<PmsProductSku>> selectSku() {
        List<PmsProductSku> list = pmsProductSkuRepository.findAll();
        if (list.isEmpty()) {
            return CommonResult.notFound();
        }
        return CommonResult.success(list, "SKU list fetched successfully");
    }

    @Override
    @Transactional
    public CommonResult<String> saveSku(Long id, List<PmsProductSku> pmsProductSkuList) {
        if (id == null || pmsProductSkuList == null) {
            return CommonResult.failed("Invalid parameters");
        }

        // Delete existing SKUs for this product
        List<PmsProductSku> existing = pmsProductSkuRepository.findByProductId(id);
        if (!existing.isEmpty()) {
            pmsProductSkuRepository.deleteAll(existing);
        }

        // Calculate total stock
        int totalStock = pmsProductSkuList.stream()
                .filter(sku -> sku.getStock() != null)
                .mapToInt(PmsProductSku::getStock)
                .sum();

        // Update product stock
        Optional<PmsProduct> productOpt = pmsProductRepository.findById(id);
        if (productOpt.isPresent()) {
            PmsProduct product = productOpt.get();
            product.setStock(totalStock);
            pmsProductRepository.save(product);
        }

        // Link product ID and timestamps
        LocalDateTime now = LocalDateTime.now();
        pmsProductSkuList.forEach(sku -> {
            sku.setProductId(id);
            if (sku.getDeletedSku() == null) {
                sku.setDeletedSku(0);
            }
            if (sku.getCreateTime() == null) {
                sku.setCreateTime(now);
            }
            sku.setUpdateTime(now);
        });

        pmsProductSkuRepository.saveAll(pmsProductSkuList);
        return CommonResult.success("Saved SKU successfully");
    }

    @Override
    public CommonResult<Page<PmsProductSku>> list(Integer page, Integer pageSize) {
        int pageNum = (page != null && page > 0) ? page - 1 : 0;
        int size = (pageSize != null && pageSize > 0) ? pageSize : 10;
        Pageable pageable = PageRequest.of(pageNum, size);

        Page<PmsProductSku> result = pmsProductSkuRepository.findByDeletedSku(0, pageable);
        return CommonResult.success(result, "SKU list queried successfully");
    }

    @Override
    public CommonResult<Page<PmsProductSku>> search(PmsSkuSearchDTO searchDTO) {
        int pageNum = (searchDTO.getPage() != null && searchDTO.getPage() > 0) ? searchDTO.getPage() - 1 : 0;
        int size = (searchDTO.getPageSize() != null && searchDTO.getPageSize() > 0) ? searchDTO.getPageSize() : 10;
        Pageable pageable = PageRequest.of(pageNum, size);

        Specification<PmsProductSku> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("deletedSku"), 0));

            if (searchDTO.getProductName() != null && !searchDTO.getProductName().isEmpty()) {
                predicates.add(cb.like(root.get("model"), "%" + searchDTO.getProductName() + "%"));
            }
            if (searchDTO.getSkuCode() != null && !searchDTO.getSkuCode().isEmpty()) {
                predicates.add(cb.equal(root.get("skuCode"), searchDTO.getSkuCode()));
            }
            if (searchDTO.getSpec() != null && !searchDTO.getSpec().isEmpty()) {
                predicates.add(cb.like(root.get("spec"), "%" + searchDTO.getSpec() + "%"));
            }
            if (searchDTO.getSkuStatus() != null && !searchDTO.getSkuStatus().isEmpty()) {
                try {
                    Integer status = Integer.parseInt(searchDTO.getSkuStatus());
                    predicates.add(cb.equal(root.get("skuStatus"), status));
                } catch (NumberFormatException ignored) {
                }
            }
            if (searchDTO.getPriceMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), BigDecimal.valueOf(searchDTO.getPriceMin())));
            }
            if (searchDTO.getPriceMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), BigDecimal.valueOf(searchDTO.getPriceMax())));
            }
            if (searchDTO.getCostMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("cost"), BigDecimal.valueOf(searchDTO.getCostMin())));
            }
            if (searchDTO.getCostMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("cost"), BigDecimal.valueOf(searchDTO.getCostMax())));
            }
            if (searchDTO.getStockMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("stock"), searchDTO.getStockMin()));
            }
            if (searchDTO.getStockMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("stock"), searchDTO.getStockMax()));
            }
            if (searchDTO.getWeightMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("weight"), BigDecimal.valueOf(searchDTO.getWeightMin())));
            }
            if (searchDTO.getWeightMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("weight"), BigDecimal.valueOf(searchDTO.getWeightMax())));
            }
            if (searchDTO.getCreateTimeStart() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createTime"), searchDTO.getCreateTimeStart()));
            }
            if (searchDTO.getCreateTimeEnd() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createTime"), searchDTO.getCreateTimeEnd()));
            }
            if (searchDTO.getUpdateTimeStart() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("updateTime"), searchDTO.getUpdateTimeStart()));
            }
            if (searchDTO.getUpdateTimeEnd() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("updateTime"), searchDTO.getUpdateTimeEnd()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<PmsProductSku> result = pmsProductSkuRepository.findAll(spec, pageable);
        log.info("SKU search result count: {}", result.getTotalElements());
        return CommonResult.success(result, "SKU search successful");
    }

    @Override
    @Transactional
    public CommonResult<String> saveSku(PmsProductSku sku) {
        if (sku == null) {
            return CommonResult.failed("Invalid SKU data");
        }
        if (sku.getSkuStatus() != null && sku.getSkuStatus() != 0 && sku.getSkuStatus() != 1) {
            return CommonResult.failed("Invalid SKU status: must be 0 or 1");
        }

        LocalDateTime now = LocalDateTime.now();
        sku.setDeletedSku(0);
        sku.setCreateTime(now);
        sku.setUpdateTime(now);

        pmsProductSkuRepository.save(sku);
        updateProductStock(sku.getProductId());

        return CommonResult.success("Added SKU successfully");
    }

    @Override
    @Transactional
    public CommonResult<String> updateSku(Long id, PmsProductSku sku) {
        if (id == null || sku == null) {
            return CommonResult.failed("Invalid parameters");
        }
        if (sku.getSkuStatus() != null && sku.getSkuStatus() != 0 && sku.getSkuStatus() != 1) {
            return CommonResult.failed("Invalid SKU status: must be 0 or 1");
        }

        Optional<PmsProductSku> existingOpt = pmsProductSkuRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return CommonResult.failed("SKU not found");
        }

        sku.setId(id);
        sku.setUpdateTime(LocalDateTime.now());
        pmsProductSkuRepository.save(sku);

        updateProductStock(sku.getProductId());
        return CommonResult.success("Updated SKU successfully");
    }

    @Override
    @Transactional
    public CommonResult<String> deleteSku(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return CommonResult.failed("IDs cannot be empty");
        }

        List<PmsProductSku> skus = pmsProductSkuRepository.findAllById(ids);
        for (PmsProductSku sku : skus) {
            sku.setDeletedSku(1);
            sku.setUpdateTime(LocalDateTime.now());
        }
        pmsProductSkuRepository.saveAll(skus);

        // Update product stock for affected products
        skus.stream().map(PmsProductSku::getProductId).distinct().forEach(this::updateProductStock);

        return CommonResult.success("1", "Deleted SKU successfully");
    }

    @Override
    public CommonResult<String> upload(MultipartFile file) {
        return commonImageService.upload(file);
    }

    private void updateProductStock(Long productId) {
        if (productId == null) return;
        List<PmsProductSku> skus = pmsProductSkuRepository.findByProductIdAndDeletedSku(productId, 0);
        int totalStock = skus.stream()
                .filter(sku -> sku.getStock() != null)
                .mapToInt(PmsProductSku::getStock)
                .sum();

        Optional<PmsProduct> productOpt = pmsProductRepository.findById(productId);
        if (productOpt.isPresent()) {
            PmsProduct product = productOpt.get();
            product.setStock(totalStock);
            pmsProductRepository.save(product);
        }
    }

    @Override
    public CommonResult<String> batchUpdateStatus(List<Long> ids, Integer status) {
        if (ids == null || ids.isEmpty() || (status != 0 && status != 1)) {
            return CommonResult.failed("Invalid parameters");
        }
        List<PmsProductSku> skus = pmsProductSkuRepository.findAllById(ids);
        LocalDateTime now = LocalDateTime.now();
        for (PmsProductSku sku : skus) {
            sku.setSkuStatus(status);
            sku.setUpdateTime(now);
        }
        pmsProductSkuRepository.saveAll(skus);
        return CommonResult.success("Batch updated SKU status successfully");
    }

    @Override
    public CommonResult<String> updateStatus(Long id, Integer status) {
        if (id == null || (status != 0 && status != 1)) {
            return CommonResult.failed("Invalid parameters");
        }
        Optional<PmsProductSku> skuOpt = pmsProductSkuRepository.findById(id);
        if (skuOpt.isEmpty()) {
            return CommonResult.failed("SKU not found");
        }
        PmsProductSku sku = skuOpt.get();
        sku.setSkuStatus(status);
        sku.setUpdateTime(LocalDateTime.now());
        pmsProductSkuRepository.save(sku);
        return CommonResult.success("Updated SKU status successfully");
    }
}
