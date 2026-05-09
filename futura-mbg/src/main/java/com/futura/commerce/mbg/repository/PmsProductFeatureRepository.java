package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.PmsProductFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for PmsProductFeature
 *
 * @author Vitalii
 */
public interface PmsProductFeatureRepository extends JpaRepository<PmsProductFeature, Long>, JpaSpecificationExecutor<PmsProductFeature> {
    List<PmsProductFeature> findByProductIdOrderBySortAsc(Long productId);
    List<PmsProductFeature> findByProductId(Long productId);
}
