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
 * Admin order review proxy controller
 *
 * @author Vitalii
 */
@RestController
@RequestMapping("/review")
@Tag(name = "AdminCommentController", description = "Order review and comment administration")
public class AdminCommentController {

    @Resource
    private OrderFeignClient orderFeignClient;

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('order:manage','review:view')")
    @Operation(summary = "Order review list", description = "Retrieve paginated order reviews via order service")
    public CommonResult<?> reviewList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return orderFeignClient.reviewList(page, pageSize);
    }
}
