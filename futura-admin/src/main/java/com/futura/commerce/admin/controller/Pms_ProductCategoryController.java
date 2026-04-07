package com.futura.commerce.admin.controller;

import com.futura.commerce.admin.dto.CategoryNode;
import com.futura.commerce.admin.service.PmsProductCategoryService;
import com.futura.commerce.common.baseCommon.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @Operation(summary = "Get category tree hierarchy", description = "Returns 3-level category tree with attached products")
    public CommonResult<List<CategoryNode>> categories() {
        return pmsProductCategoryService.getCategoriesList();
    }
}
