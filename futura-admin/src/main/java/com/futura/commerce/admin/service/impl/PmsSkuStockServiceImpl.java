package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.service.PmsSkuStockService;
import com.futura.commerce.mbg.model.PmsSkuStock;
import com.futura.commerce.mbg.repository.PmsSkuStockRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing PmsSkuStock
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class PmsSkuStockServiceImpl implements PmsSkuStockService {

    @Resource
    private PmsSkuStockRepository pmsSkuStockRepository;

    @Override
    public List<PmsSkuStock> findAll() {
        return pmsSkuStockRepository.findAll();
    }

    @Override
    public Optional<PmsSkuStock> findById(Long id) {
        return pmsSkuStockRepository.findById(id);
    }

    @Override
    public PmsSkuStock save(PmsSkuStock entity) {
        return pmsSkuStockRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        pmsSkuStockRepository.deleteById(id);
    }
}
