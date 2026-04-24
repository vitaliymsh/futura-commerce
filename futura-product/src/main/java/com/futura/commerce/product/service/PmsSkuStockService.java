package com.futura.commerce.product.service;

import com.futura.commerce.mbg.model.PmsSkuStock;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing PmsSkuStock
 *
 * @author Vitalii
 */
public interface PmsSkuStockService {

    List<PmsSkuStock> findAll();

    Optional<PmsSkuStock> findById(Long id);

    PmsSkuStock save(PmsSkuStock entity);

    void deleteById(Long id);
}
