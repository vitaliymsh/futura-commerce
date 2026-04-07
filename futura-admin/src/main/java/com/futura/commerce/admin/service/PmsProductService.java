package com.futura.commerce.admin.service;

import com.futura.commerce.admin.dto.IsPromotionVO;
import com.futura.commerce.common.baseCommon.CommonResult;
import com.futura.commerce.mbg.model.PmsProduct;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing PmsProduct
 *
 * @author Vitalii
 */
public interface PmsProductService {

    List<PmsProduct> findAll();

    Optional<PmsProduct> findById(Long id);

    PmsProduct save(PmsProduct entity);

    void deleteById(Long id);

    CommonResult<String> isOpen(IsPromotionVO promotionVO);
}
