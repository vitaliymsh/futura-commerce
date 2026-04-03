package com.futura.commerce.admin.service;

import com.futura.commerce.mbg.model.UmsUserPromotion;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing UmsUserPromotion
 *
 * @author Vitalii
 */
public interface UmsUserPromotionService {

    List<UmsUserPromotion> findAll();

    Optional<UmsUserPromotion> findById(Long id);

    UmsUserPromotion save(UmsUserPromotion entity);

    void deleteById(Long id);
}
