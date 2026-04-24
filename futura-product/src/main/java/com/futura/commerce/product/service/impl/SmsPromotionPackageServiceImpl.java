package com.futura.commerce.product.service.impl;

import com.futura.commerce.product.service.SmsPromotionPackageService;
import com.futura.commerce.mbg.model.SmsPromotionPackage;
import com.futura.commerce.mbg.repository.SmsPromotionPackageRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing SmsPromotionPackage
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class SmsPromotionPackageServiceImpl implements SmsPromotionPackageService {

    @Resource
    private SmsPromotionPackageRepository smsPromotionPackageRepository;

    @Override
    public List<SmsPromotionPackage> findAll() {
        return smsPromotionPackageRepository.findAll();
    }

    @Override
    public Optional<SmsPromotionPackage> findById(Long id) {
        return smsPromotionPackageRepository.findById(id);
    }

    @Override
    public SmsPromotionPackage save(SmsPromotionPackage entity) {
        return smsPromotionPackageRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        smsPromotionPackageRepository.deleteById(id);
    }
}
