package com.futura.commerce.admin.controller;

import com.futura.commerce.admin.dto.PmsSkuSearchDTO;
import com.futura.commerce.admin.service.PmsProductSkuService;
import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.PmsProductSku;
import com.futura.commerce.mbg.model.PmsSkuPriceHistory;
import com.futura.commerce.mbg.model.SysOperationLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Product SKU management controller
 *
 * @author Vitalii
 */
@Slf4j
@RestController
@RequestMapping("/Sku")
@Tag(name = "Pms_ProductSkuController", description = "SKU management")
public class Pms_ProductSkuController {

    @Resource
    private PmsProductSkuService productSkuService;

    /**
     * Display paginated SKU list
     */
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','sku:view')")
    @Operation(summary = "Display SKU list", description = "Retrieve paginated list of active SKUs")
    public CommonResult<Page<PmsProductSku>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return productSkuService.list(page, pageSize);
    }

    /**
     * Search SKUs by multiple criteria
     */
    @PostMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','sku:view')")
    @Operation(summary = "Multi-field search for SKUs", description = "Aggregated search and exact matching for SKU list")
    public CommonResult<Page<PmsProductSku>> search(@RequestBody PmsSkuSearchDTO searchDTO) {
        return productSkuService.search(searchDTO);
    }

    /**
     * Add new SKU
     */
    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','sku:edit')")
    @Operation(summary = "Create SKU", description = "Create and save a new SKU")
    public CommonResult<String> update(@RequestBody PmsProductSku sku) {
        return productSkuService.saveSku(sku);
    }

    /**
     * Edit existing SKU
     */
    @PutMapping("/save/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','sku:edit')")
    @Operation(summary = "Edit SKU", description = "Update existing SKU details by ID")
    public CommonResult<String> save(@PathVariable Long id, @RequestBody PmsProductSku sku) {
        return productSkuService.updateSku(id, sku);
    }

    /**
     * Delete SKUs (supports batch deletion)
     */
    @PostMapping("/del")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','sku:edit')")
    @Operation(summary = "Delete SKUs", description = "Batch delete SKUs by IDs")
    public CommonResult<String> del(@RequestBody Map<String, List<Long>> ids) {
        return productSkuService.deleteSku(ids.get("ids"));
    }

    /**
     * Upload SKU image
     */
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','sku:edit')")
    @Operation(summary = "Upload SKU image", description = "Upload image file for SKU")
    public CommonResult<String> upload(@RequestParam("file") MultipartFile file) {
        return productSkuService.upload(file);
    }

    /**
     * Batch publish SKUs
     */
    @PostMapping("/batch/up")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','sku:edit')")
    @Operation(summary = "Batch publish SKUs", description = "Batch set SKU status to published")
    public CommonResult<String> batchUp(@RequestBody Map<String, List<Long>> ids) {
        return productSkuService.batchUpdateStatus(ids.get("ids"), 1);
    }

    /**
     * Batch unpublish SKUs
     */
    @PostMapping("/batch/down")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','sku:edit')")
    @Operation(summary = "Batch unpublish SKUs", description = "Batch set SKU status to unpublished")
    public CommonResult<String> batchDown(@RequestBody Map<String, List<Long>> ids) {
        return productSkuService.batchUpdateStatus(ids.get("ids"), 0);
    }

    /**
     * Update individual SKU status
     */
    @PutMapping("/status/{id}/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','sku:edit')")
    @Operation(summary = "Update SKU status", description = "Toggle publish status for individual SKU")
    public CommonResult<String> updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        return productSkuService.updateStatus(id, status);
    }

    /**
     * Query SKU operation logs
     */
    @GetMapping("/logs")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','sku:view')")
    @Operation(summary = "Query SKU operation logs", description = "Retrieve operation log history for a specific SKU")
    public CommonResult<List<SysOperationLog>> getSkuLogs(@RequestParam Long skuId) {
        return productSkuService.getSkuLogs(skuId);
    }

    /**
     * Query SKU price change history
     */
    @GetMapping("/price-history")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','sku:view')")
    @Operation(summary = "Query SKU price history", description = "Retrieve price history records for a specific SKU")
    public CommonResult<List<PmsSkuPriceHistory>> getPriceHistory(@RequestParam Long skuId) {
        return productSkuService.getPriceHistory(skuId);
    }
}
