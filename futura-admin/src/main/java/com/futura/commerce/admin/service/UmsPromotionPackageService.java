package com.futura.commerce.admin.service;

import com.futura.commerce.mbg.model.UmsPromotionPackage;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing UmsPromotionPackage
 *
 * @author Vitalii
 */
public interface UmsPromotionPackageService {

    List<UmsPromotionPackage> findAll();

    Optional<UmsPromotionPackage> findById(Long id);

    UmsPromotionPackage save(UmsPromotionPackage entity);

    void deleteById(Long id);
}
