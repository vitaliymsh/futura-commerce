package com.futura.commerce.product.controller;

import com.futura.commerce.common.baseCommon.CommonResult;
import com.futura.commerce.product.dto.ProductDetailDTO;
import com.futura.commerce.product.service.PmsProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Customer product management controller
 *
 * @author Vitalii
 */
@Slf4j
@RestController
@RequestMapping("/user/product")
@Tag(name = "UserProductController", description = "Customer product details")
public class UserProductController {

    @Resource
    private PmsProductService pmsProductService;

    @GetMapping("/detail")
    @Operation(summary = "Product detail", description = "Get product specification parameters and feature highlights")
    public CommonResult<ProductDetailDTO> detail(@RequestParam("id") Long productId) {
        return pmsProductService.detail(productId);
    }

    @GetMapping("/recommend")
    @Operation(summary = "Personalized recommendations", description = "Personalized product recommendations based on browsing history and Bloom filter")
    public CommonResult<org.springframework.data.domain.Page<com.futura.commerce.product.dto.ProductSkuEsDoc>> productRecommend(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return pmsProductService.productRecommend(pageNum, pageSize);
    }
}
