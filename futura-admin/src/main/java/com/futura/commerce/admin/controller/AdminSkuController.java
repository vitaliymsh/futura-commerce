package com.futura.commerce.admin.controller;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.feign.product.ProductFeignClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Admin SKU management controller forwarding to product microservice
 *
 * @author Vitalii
 */
@RestController
@RequestMapping("/Sku")
@Tag(name = "AdminSkuController", description = "Admin product SKU management")
public class AdminSkuController {

    @Resource
    private ProductFeignClient productFeignClient;

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','sku:view')")
    @Operation(summary = "SKU list", description = "Get paginated SKU items")
    public CommonResult<?> skuList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return productFeignClient.skuList(page, pageSize);
    }

    @PostMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','sku:view')")
    @Operation(summary = "Search SKU", description = "Search SKU by criteria")
    public CommonResult<?> skuSearch(@RequestBody Object searchDTO) {
        return productFeignClient.skuSearch(searchDTO);
    }

    @PutMapping("/save/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','sku:edit')")
    @Operation(summary = "Save SKU", description = "Save SKU changes by id")
    public CommonResult<String> saveSku(@PathVariable Long id, @RequestBody Object sku) {
        return productFeignClient.saveSku(id, sku);
    }

    @PostMapping("/del")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','sku:delete')")
    @Operation(summary = "Delete SKU", description = "Batch delete SKUs by id list")
    public CommonResult<String> delSku(@RequestBody Map<String, List<Long>> ids) {
        return productFeignClient.delSku(ids);
    }
}
