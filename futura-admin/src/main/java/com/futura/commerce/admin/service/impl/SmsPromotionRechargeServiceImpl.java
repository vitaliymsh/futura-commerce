package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.service.SmsPromotionRechargeService;
import com.futura.commerce.mbg.model.SmsPromotionRecharge;
import com.futura.commerce.mbg.repository.SmsPromotionRechargeRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for promotion recharge records
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class SmsPromotionRechargeServiceImpl implements SmsPromotionRechargeService {

    @Resource
    private SmsPromotionRechargeRepository smsPromotionRechargeRepository;

    @Override
    public List<SmsPromotionRecharge> findAll() {
        return smsPromotionRechargeRepository.findAll();
    }

    @Override
    public Optional<SmsPromotionRecharge> findById(Long id) {
        return smsPromotionRechargeRepository.findById(id);
    }

    @Override
    public SmsPromotionRecharge save(SmsPromotionRecharge entity) {
        return smsPromotionRechargeRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        smsPromotionRechargeRepository.deleteById(id);
    }

    @Override
    public List<SmsPromotionRecharge> findByAdminId(Long adminId) {
        return smsPromotionRechargeRepository.findByAdminIdOrderByRechargeTimeDesc(adminId);
    }
}
