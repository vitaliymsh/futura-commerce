package com.futura.commerce.product.service.impl;

import com.futura.commerce.mbg.model.PmsProductFeature;
import com.futura.commerce.mbg.repository.PmsProductFeatureRepository;
import com.futura.commerce.product.service.PmsProductFeatureService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing PmsProductFeature
 *
 * @author Vitalii
 */
@Service
public class PmsProductFeatureServiceImpl implements PmsProductFeatureService {

    @Resource
    private PmsProductFeatureRepository pmsProductFeatureRepository;

    @Override
    public List<PmsProductFeature> findByProductId(Long productId) {
        return pmsProductFeatureRepository.findByProductIdOrderBySortAsc(productId);
    }

    @Override
    public PmsProductFeature save(PmsProductFeature feature) {
        return pmsProductFeatureRepository.save(feature);
    }

    @Override
    public void deleteById(Long id) {
        pmsProductFeatureRepository.deleteById(id);
    }
}
