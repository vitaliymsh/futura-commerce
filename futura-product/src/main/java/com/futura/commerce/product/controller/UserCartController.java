package com.futura.commerce.product.controller;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.UmsCart;
import com.futura.commerce.product.service.UmsCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Product cart controller
 *
 * @author Vitalii
 */
@Slf4j
@RestController
@RequestMapping("/user/cart")
@Tag(name = "UserCartController", description = "Customer shopping cart operations")
public class UserCartController {

    @Resource
    private UmsCartService umsCartService;

    @GetMapping("/list")
    @Operation(summary = "Get cart item list", description = "Retrieve all items currently stored in customer shopping cart")
    public CommonResult<List<UmsCart>> cartList() {
        return umsCartService.cartList();
    }
}
