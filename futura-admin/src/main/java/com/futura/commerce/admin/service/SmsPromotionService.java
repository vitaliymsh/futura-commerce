package com.futura.commerce.admin.service;

import com.futura.commerce.admin.dto.GoodsQuotaRequestVO;
import com.futura.commerce.admin.dto.SmsPromotionProductVO;
import com.futura.commerce.common.baseCommon.CommonResult;
import com.futura.commerce.mbg.model.SmsPromotion;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for campaign promotions
 *
 * @author Vitalii
 */
public interface SmsPromotionService {

    List<SmsPromotion> findAll();

    Optional<SmsPromotion> findById(Long id);

    SmsPromotion save(SmsPromotion entity);

    void deleteById(Long id);

    CommonResult<List<SmsPromotionProductVO>> getPromotionProductList();

    CommonResult<List<SmsPromotionProductVO>> createGoodsQuota(GoodsQuotaRequestVO goodsPromotionVO, Long packageId);

    CommonResult<String> delGoods(List<Long> productIds);
}
