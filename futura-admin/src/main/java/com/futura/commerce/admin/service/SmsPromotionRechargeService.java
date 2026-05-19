package com.futura.commerce.admin.service;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.SmsPromotionRecharge;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for promotion recharge history
 *
 * @author Vitalii
 */
public interface SmsPromotionRechargeService {

    List<SmsPromotionRecharge> findAll();

    Optional<SmsPromotionRecharge> findById(Long id);

    SmsPromotionRecharge save(SmsPromotionRecharge entity);

    void deleteById(Long id);

    List<SmsPromotionRecharge> findByAdminId(Long adminId);
}
