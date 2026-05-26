package com.futura.commerce.user.controller;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.feign.product.ProductFeignClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Customer product details proxy controller
 *
 * @author Vitalii
 */
@RestController
@RequestMapping("/user/product")
@Tag(name = "UserProductController", description = "Customer product details")
public class UserProductController {

    @Resource
    private ProductFeignClient productFeignClient;

    @GetMapping("/detail")
    @Operation(summary = "Product detail", description = "Retrieve product specifications and highlights via product service")
    public CommonResult<?> detail(@RequestParam("id") Long productId) {
        return productFeignClient.detail(productId);
    }

    @GetMapping("/recommend")
    @Operation(summary = "Product recommendations", description = "Retrieve personalized product recommendations")
    public CommonResult<?> recommend(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return productFeignClient.productRecommend(pageNum, pageSize);
    }
}
