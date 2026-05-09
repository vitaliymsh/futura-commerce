package com.futura.commerce.product.service;

import com.futura.commerce.mbg.model.PmsProductFeature;

import java.util.List;

/**
 * Service interface for managing PmsProductFeature
 *
 * @author Vitalii
 */
public interface PmsProductFeatureService {

    List<PmsProductFeature> findByProductId(Long productId);

    PmsProductFeature save(PmsProductFeature feature);

    void deleteById(Long id);
}
