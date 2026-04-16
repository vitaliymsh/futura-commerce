package com.futura.commerce.admin.controller;

import com.futura.commerce.admin.dto.CategoryNode;
import com.futura.commerce.admin.service.PmsProductCategoryService;
import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.PmsProductCategory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Product category management controller
 *
 * @author Vitalii
 */
@Slf4j
@RestController
@RequestMapping("/goods")
@Tag(name = "ProductCategoryController", description = "Product category tree and classification")
public class Pms_ProductCategoryController {

    @Resource
    private PmsProductCategoryService pmsProductCategoryService;

    /**
     * Get 3-level product category hierarchy tree
     */
    @GetMapping("/categories")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','promotion:view')")
    @Operation(summary = "Get category tree hierarchy", description = "Returns 3-level category tree with attached products")
    public CommonResult<List<CategoryNode>> categories() {
        return pmsProductCategoryService.getCategoriesList();
    }

    /**
     * Create category
     */
    @PostMapping("/categories/add")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','category:edit')")
    @Operation(summary = "Create category", description = "Add a new product category")
    public CommonResult<String> addCategory(@RequestBody PmsProductCategory category) {
        return pmsProductCategoryService.addCategory(category);
    }

    /**
     * Update category
     */
    @PutMapping("/categories/update/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','category:edit')")
    @Operation(summary = "Update category", description = "Update existing product category by ID")
    public CommonResult<String> updateCategory(@PathVariable Long id, @RequestBody PmsProductCategory category) {
        return pmsProductCategoryService.updateCategory(id, category);
    }

    /**
     * Delete category
     */
    @DeleteMapping("/categories/del/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','category:edit')")
    @Operation(summary = "Delete category", description = "Delete product category by ID if no dependents exist")
    public CommonResult<String> deleteCategory(@PathVariable Long id) {
        return pmsProductCategoryService.deleteCategory(id);
    }
}
