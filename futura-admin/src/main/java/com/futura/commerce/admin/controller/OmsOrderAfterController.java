package com.futura.commerce.admin.controller;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.feign.order.OrderFeignClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin order after-sales proxy controller
 *
 * @author Vitalii
 */
@RestController
@RequestMapping("/after")
@Tag(name = "OmsOrderAfterController", description = "Order after-sales customer service administration")
public class OmsOrderAfterController {

    @Resource
    private OrderFeignClient orderFeignClient;

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('order:manage','afterSales:view')")
    @Operation(summary = "After-sales request list", description = "Retrieve paginated order after-sales applications")
    public CommonResult<?> orderAfterList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return orderFeignClient.orderAfterList(page, pageSize);
    }
}
