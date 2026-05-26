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
 * Customer category proxy controller
 *
 * @author Vitalii
 */
@RestController
@RequestMapping("/user")
@Tag(name = "UserCategoryController", description = "Customer product categories proxy")
public class UserCategoryController {

    @Resource
    private ProductFeignClient productFeignClient;

    @GetMapping("/category/list")
    @Operation(summary = "Category list", description = "Get category tree hierarchy")
    public CommonResult<?> categoryList() {
        return productFeignClient.productCategoryList();
    }
}
