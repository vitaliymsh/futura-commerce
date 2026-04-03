package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.service.PmsProductCategoryService;
import com.futura.commerce.mbg.model.PmsProductCategory;
import com.futura.commerce.mbg.repository.PmsProductCategoryRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing PmsProductCategory
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class PmsProductCategoryServiceImpl implements PmsProductCategoryService {

    @Resource
    private PmsProductCategoryRepository pmsProductCategoryRepository;

    @Override
    public List<PmsProductCategory> findAll() {
        return pmsProductCategoryRepository.findAll();
    }

    @Override
    public Optional<PmsProductCategory> findById(Long id) {
        return pmsProductCategoryRepository.findById(id);
    }

    @Override
    public PmsProductCategory save(PmsProductCategory entity) {
        return pmsProductCategoryRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        pmsProductCategoryRepository.deleteById(id);
    }
}
