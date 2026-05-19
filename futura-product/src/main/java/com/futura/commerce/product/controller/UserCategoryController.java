package com.futura.commerce.product.controller;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.product.dto.CategoryNode;
import com.futura.commerce.product.dto.ClickDTO;
import com.futura.commerce.product.service.PmsProductCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Customer category controller
 *
 * @author Vitalii
 */
@Slf4j
@RestController
@RequestMapping("/user/category")
@Tag(name = "UserCategoryController", description = "Customer product category tree and click tracking")
public class UserCategoryController {

    @Resource
    private PmsProductCategoryService pmsProductCategoryService;

    @GetMapping("/list")
    @Operation(summary = "Category list", description = "Customer category tree hierarchy")
    public CommonResult<List<CategoryNode>> categoryList() {
        return pmsProductCategoryService.getCategoriesList();
    }

    @PostMapping("/click/report")
    @Operation(summary = "Product click tracking report", description = "Report customer product category click behavior")
    public CommonResult<String> productClickReport(@RequestBody ClickDTO clickDTO) {
        return pmsProductCategoryService.productClickReport(clickDTO);
    }
}
