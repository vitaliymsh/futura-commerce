package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.service.UmsUserPromotionService;
import com.futura.commerce.mbg.model.UmsUserPromotion;
import com.futura.commerce.mbg.repository.UmsUserPromotionRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing UmsUserPromotion
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class UmsUserPromotionServiceImpl implements UmsUserPromotionService {

    @Resource
    private UmsUserPromotionRepository umsUserPromotionRepository;

    @Override
    public List<UmsUserPromotion> findAll() {
        return umsUserPromotionRepository.findAll();
    }

    @Override
    public Optional<UmsUserPromotion> findById(Long id) {
        return umsUserPromotionRepository.findById(id);
    }

    @Override
    public UmsUserPromotion save(UmsUserPromotion entity) {
        return umsUserPromotionRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        umsUserPromotionRepository.deleteById(id);
    }
}
