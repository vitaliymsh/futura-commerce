package com.futura.commerce.admin.service;

import com.futura.commerce.mbg.model.SmsPromotionPackage;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing SmsPromotionPackage
 *
 * @author Vitalii
 */
public interface SmsPromotionPackageService {

    List<SmsPromotionPackage> findAll();

    Optional<SmsPromotionPackage> findById(Long id);

    SmsPromotionPackage save(SmsPromotionPackage entity);

    void deleteById(Long id);
}
