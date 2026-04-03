package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.PmsSkuStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for PmsSkuStock
 *
 * @author Vitalii
 */
public interface PmsSkuStockRepository extends JpaRepository<PmsSkuStock, Long>, JpaSpecificationExecutor<PmsSkuStock> {
}
