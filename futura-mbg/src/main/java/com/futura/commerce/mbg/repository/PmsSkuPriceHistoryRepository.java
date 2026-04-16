package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.PmsSkuPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for PmsSkuPriceHistory entity
 *
 * @author Vitalii
 */
@Repository
public interface PmsSkuPriceHistoryRepository extends JpaRepository<PmsSkuPriceHistory, Long> {

    List<PmsSkuPriceHistory> findBySkuId(Long skuId);
}
