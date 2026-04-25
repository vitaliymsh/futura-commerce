package com.futura.commerce.feign.product;

import com.futura.commerce.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Feign client interface for Futura Product service
 *
 * @author Vitalii
 */
@FeignClient(name = "futura-product", contextId = "productClient")
public interface ProductFeignClient {

    @GetMapping("/Pms_promotion/goodList")
    CommonResult<?> goodsList();

    @GetMapping("/Pms_promotion/goodsPagination")
    CommonResult<?> goodsPagination(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer pageSize);

    @GetMapping("/Pms_promotion/search")
    CommonResult<?> search(@RequestParam(value = "page", defaultValue = "1") Integer page,
                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                           @RequestParam(value = "publishStatus", required = false) Integer status,
                           @RequestParam(value = "name", required = false) String keySearch,
                           @RequestParam(value = "categoryId", required = false) Integer categoryId);

    @PutMapping("/Pms_promotion/save")
    CommonResult<?> save(@RequestBody Object promotion);

    @PostMapping("/Pms_promotion/upload")
    CommonResult<String> upload(@RequestParam("file") MultipartFile file);

    @DeleteMapping("/Pms_promotion/delete/{id}")
    CommonResult<String> delete(@PathVariable Long id);

    @PutMapping("/Pms_promotion/status/{id}/{status}")
    CommonResult<String> updateStatus(@PathVariable Long id, @PathVariable Integer status);

    @PutMapping("/Pms_promotion/updateProduct/{id}")
    CommonResult<String> updateProduct(@PathVariable Long id, @RequestBody Object promotion);

    @PutMapping("/Pms_promotion/updateSku/{id}")
    CommonResult<String> updateSku(@PathVariable Long id, @RequestBody List<Object> pmsProductSkuList);

    @GetMapping("/Pms_promotion/selectSku")
    CommonResult<?> selectSku();

    // Category endpoints
    @GetMapping("/goods/categories")
    CommonResult<?> categories();

    @PostMapping("/goods/categories/add")
    CommonResult<String> addCategory(@RequestBody Object category);

    @PutMapping("/goods/categories/update/{id}")
    CommonResult<String> updateCategory(@PathVariable Long id, @RequestBody Object category);

    @DeleteMapping("/goods/categories/del/{id}")
    CommonResult<String> deleteCategory(@PathVariable Long id);

    // Dashboard statistics endpoint
    @GetMapping("/dashboard/stats")
    CommonResult<Map<String, Object>> stat(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate);

    // Sku endpoints
    @GetMapping("/Sku/list")
    CommonResult<?> skuList(@RequestParam(defaultValue = "1") Integer page,
                            @RequestParam(defaultValue = "10") Integer pageSize);

    @PostMapping("/Sku/search")
    CommonResult<?> skuSearch(@RequestBody Object searchDTO);

    @PutMapping("/Sku/save/{id}")
    CommonResult<String> saveSku(@PathVariable Long id, @RequestBody Object sku);

    @PostMapping("/Sku/del")
    CommonResult<String> delSku(@RequestBody Map<String, List<Long>> ids);
}
