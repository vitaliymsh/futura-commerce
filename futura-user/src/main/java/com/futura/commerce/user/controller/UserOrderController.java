package com.futura.commerce.user.controller;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.feign.order.OrderFeignClient;
import com.futura.commerce.feign.product.ProductFeignClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * Customer order and interaction proxy controller
 *
 * @author Vitalii
 */
@RestController
@RequestMapping("/user")
@Tag(name = "UserOrderController", description = "Customer order reviews and interaction events")
public class UserOrderController {

    @Resource
    private OrderFeignClient orderFeignClient;

    @Resource
    private ProductFeignClient productFeignClient;

    @GetMapping("/comment/{productId}")
    @Operation(summary = "Get product comments", description = "Get customer reviews for a given product")
    public CommonResult<?> userComment(@PathVariable Long productId) {
        return orderFeignClient.orderComment(productId);
    }

    @PostMapping("/category/click/report")
    @Operation(summary = "Category click tracking report", description = "Forward category click analytics")
    public CommonResult<?> userCategoryClickReport(@RequestBody Object dto) {
        return productFeignClient.productClickReport(dto);
    }
}
