package com.futura.commerce.product.controller;

import com.futura.commerce.product.dto.IsPromotionDTO;
import com.futura.commerce.product.dto.PmsPromotionSearchDTO;
import com.futura.commerce.product.dto.PmsPromotionVO;
import com.futura.commerce.product.service.PmsProductService;
import com.futura.commerce.product.service.PmsProductSkuService;
import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.PmsProduct;
import com.futura.commerce.mbg.model.PmsProductSku;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Product management controller
 *
 * @author Vitalii
 */
@Slf4j
@RestController
@RequestMapping("/Pms_promotion")
@Tag(name = "Pms_ProductController", description = "Product management")
public class Pms_ProductController {

    @Resource
    private PmsProductService promotionService;

    @Resource
    private PmsProductSkuService promotionSkuService;

    /**
     * Enable or disable product promotion status
     */
    @PostMapping("/is_open")
    @Operation(summary = "Toggle product promotion status", description = "Switch promotion status on or off for a product")
    public CommonResult<String> isOpen(@RequestBody IsPromotionDTO promotionVO) {
        return promotionService.isOpen(promotionVO);
    }

    /**
     * Product list
     */
    @GetMapping("/goodList")
    @Operation(summary = "Product list", description = "Retrieve list of active promotion products")
    public CommonResult<List<PmsPromotionVO>> goodsList() {
        return promotionService.goodsList();
    }

    /**
     * Paginated product list
     */
    @GetMapping("/goodsPagination")
    @Operation(summary = "Paginated product list", description = "Retrieve paginated list of active promotion products")
    public CommonResult<Page<PmsPromotionVO>> goodsPagination(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return promotionService.goodsPagination(page, pageSize);
    }

    /**
     * Search products
     */
    @GetMapping("/search")
    @Operation(summary = "Search products by conditions")
    public CommonResult<Page<PmsProduct>> search(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "publishStatus", required = false) Integer status,
            @RequestParam(value = "name", required = false) String keySearch,
            @RequestParam(value = "categoryId", required = false) Integer categoryId) {
        return promotionService.getPromotionByKeySearch(page, pageSize, status, keySearch, categoryId);
    }

    /**
     * Save product details
     */
    @PutMapping("/save")
    @Operation(summary = "Save or update product info")
    public CommonResult<PmsPromotionSearchDTO> save(@RequestBody PmsPromotionSearchDTO promotion) {
        return promotionService.getPromotionSave(promotion);
    }

    /**
     * Upload product image
     */
    @PostMapping("/upload")
    @Operation(summary = "Upload product image")
    public CommonResult<String> upload(@RequestParam("file") MultipartFile file) {
        return promotionService.upload(file);
    }

    /**
     * Delete product
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete product")
    public CommonResult<String> delete(@PathVariable Long id) {
        return promotionService.delete(id);
    }

    /**
     * Toggle product publish status
     */
    @PutMapping("/status/{id}/{status}")
    @Operation(summary = "Toggle product publish status")
    public CommonResult<String> updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        return promotionService.updateStatus(id, status);
    }

    /**
     * Update product details
     */
    @PutMapping("/updateProduct/{id}")
    @Operation(summary = "Update product details")
    public CommonResult<String> updateProduct(@PathVariable Long id, @RequestBody PmsPromotionSearchDTO promotion) {
        return promotionService.updateProduct(id, promotion);
    }

    /**
     * Update product SKU list
     */
    @PutMapping("/updateSku/{id}")
    @Operation(summary = "Update product SKU details")
    public CommonResult<String> updateSku(@PathVariable Long id, @RequestBody List<PmsProductSku> pmsProductSkuList) {
        return promotionSkuService.updateSku(id, pmsProductSkuList);
    }

    /**
     * Retrieve all product SKUs
     */
    @GetMapping("/selectSku")
    @Operation(summary = "Retrieve product SKU details")
    public CommonResult<List<PmsProductSku>> selectSku() {
        return promotionSkuService.selectSku();
    }
}
