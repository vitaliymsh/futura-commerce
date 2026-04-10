package com.futura.commerce.admin.controller;

import com.futura.commerce.admin.dto.IsPromotionDTO;
import com.futura.commerce.admin.dto.PmsPromotionSearchDTO;
import com.futura.commerce.admin.dto.PmsPromotionVO;
import com.futura.commerce.admin.service.PmsProductService;
import com.futura.commerce.common.baseCommon.CommonResult;
import com.futura.commerce.mbg.model.PmsProduct;
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
 * Product promotion and management controller
 *
 * @author Vitalii
 */
@Slf4j
@RestController
@RequestMapping("/Pms_promotion")
@Tag(name = "Pms_PromotionController", description = "Product management and promotion toggles")
public class Pms_PromotionController {

    @Resource
    private PmsProductService promotionService;

    /**
     * Enable or disable product promotion status
     */
    @PostMapping("/is_open")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','promotion:view')")
    @Operation(summary = "Toggle product promotion status", description = "Switch promotion status on or off for a product")
    public CommonResult<String> isOpen(@RequestBody IsPromotionDTO promotionVO) {
        return promotionService.isOpen(promotionVO);
    }

    @GetMapping("/goodList")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','promotion:view')")
    @Operation(summary = "List all products for promotion")
    public CommonResult<List<PmsPromotionVO>> goodsList() {
        return promotionService.goodsList();
    }

    @GetMapping("/goodsPagination")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','promotion:view')")
    @Operation(summary = "Paginated products list")
    public CommonResult<Page<PmsPromotionVO>> goodsPagination(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return promotionService.goodsPagination(page, pageSize);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','promotion:view')")
    @Operation(summary = "Search products by conditions")
    public CommonResult<Page<PmsProduct>> search(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "publishStatus", required = false) Integer status,
            @RequestParam(value = "name", required = false) String keySearch,
            @RequestParam(value = "categoryId", required = false) Integer categoryId) {
        return promotionService.getPromotionByKeySearch(page, pageSize, status, keySearch, categoryId);
    }

    @PutMapping("/save")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','promotion:view')")
    @Operation(summary = "Save or update product info")
    public CommonResult<PmsPromotionSearchDTO> save(@RequestBody PmsPromotionSearchDTO promotion) {
        return promotionService.getPromotionSave(promotion);
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','promotion:view')")
    @Operation(summary = "Upload product image")
    public CommonResult<String> upload(@RequestParam("file") MultipartFile file) {
        return promotionService.upload(file);
    }
}
