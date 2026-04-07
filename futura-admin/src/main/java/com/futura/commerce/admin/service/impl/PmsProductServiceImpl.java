package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.dto.IsPromotionVO;
import com.futura.commerce.admin.service.PmsProductService;
import com.futura.commerce.common.baseCommon.CommonResult;
import com.futura.commerce.mbg.model.PmsProduct;
import com.futura.commerce.mbg.repository.PmsProductRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public CommonResult<String> isOpen(IsPromotionVO promotionVO) {
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
}
