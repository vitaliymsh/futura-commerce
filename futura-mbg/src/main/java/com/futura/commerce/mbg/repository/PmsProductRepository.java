package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.PmsProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for PmsProduct
 *
 * @author Vitalii
 */
public interface PmsProductRepository extends JpaRepository<PmsProduct, Long>, JpaSpecificationExecutor<PmsProduct> {
    long countByCategoryId(Long categoryId);
}
