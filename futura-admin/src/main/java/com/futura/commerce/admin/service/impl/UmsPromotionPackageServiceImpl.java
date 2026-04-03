package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.service.UmsPromotionPackageService;
import com.futura.commerce.mbg.model.UmsPromotionPackage;
import com.futura.commerce.mbg.repository.UmsPromotionPackageRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing UmsPromotionPackage
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class UmsPromotionPackageServiceImpl implements UmsPromotionPackageService {

    @Resource
    private UmsPromotionPackageRepository umsPromotionPackageRepository;

    @Override
    public List<UmsPromotionPackage> findAll() {
        return umsPromotionPackageRepository.findAll();
    }

    @Override
    public Optional<UmsPromotionPackage> findById(Long id) {
        return umsPromotionPackageRepository.findById(id);
    }

    @Override
    public UmsPromotionPackage save(UmsPromotionPackage entity) {
        return umsPromotionPackageRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        umsPromotionPackageRepository.deleteById(id);
    }
}
