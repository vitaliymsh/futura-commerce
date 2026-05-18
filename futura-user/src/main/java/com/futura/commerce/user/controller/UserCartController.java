package com.futura.commerce.user.controller;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.feign.product.ProductFeignClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Customer shopping cart proxy controller
 *
 * @author Vitalii
 */
@RestController
@RequestMapping("/user")
@Tag(name = "UserCartController", description = "Customer shopping cart endpoint proxy")
public class UserCartController {

    @Resource
    private ProductFeignClient productFeignClient;

    @GetMapping("/cart/list")
    @Operation(summary = "Get customer cart", description = "Query shopping cart list for current authenticated user")
    public CommonResult<?> cartList() {
        return productFeignClient.cartList();
    }
}
