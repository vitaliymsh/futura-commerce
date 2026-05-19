package com.futura.commerce.admin.service;

import com.futura.commerce.admin.dto.CategoryNode;
import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.PmsProductCategory;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing PmsProductCategory
 *
 * @author Vitalii
 */
public interface PmsProductCategoryService {

    List<PmsProductCategory> findAll();

    Optional<PmsProductCategory> findById(Long id);

    PmsProductCategory save(PmsProductCategory entity);

    void deleteById(Long id);

    CommonResult<List<CategoryNode>> getCategoriesList();

    CommonResult<String> addCategory(PmsProductCategory category);

    CommonResult<String> updateCategory(Long id, PmsProductCategory category);

    CommonResult<String> deleteCategory(Long id);
}
