package com.futura.commerce.product.service.impl;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.UmsCart;
import com.futura.commerce.mbg.repository.UmsCartRepository;
import com.futura.commerce.product.service.UmsCartService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for customer cart operations
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class UmsCartServiceImpl implements UmsCartService {

    @Resource
    private UmsCartRepository umsCartRepository;

    @Override
    public CommonResult<List<UmsCart>> cartList() {
        try {
            List<UmsCart> list = umsCartRepository.findAll();
            if (list == null || list.isEmpty()) {
                return CommonResult.success(new java.util.ArrayList<>(), "Shopping cart is empty");
            }
            return CommonResult.success(list, "Cart items retrieved successfully");
        } catch (Exception e) {
            log.warn("Cart query exception, returning empty cart: {}", e.getMessage());
            return CommonResult.success(new java.util.ArrayList<>(), "Shopping cart is empty");
        }
    }
}
