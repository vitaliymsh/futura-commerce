package com.futura.commerce.admin.controller;

import com.futura.commerce.admin.service.OmsOrderService;
import com.futura.commerce.admin.vo.OmsOrderVO;
import com.futura.commerce.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Order details and operations controller
 *
 * @author Vitalii
 */
@Slf4j
@RestController
@RequestMapping("/order")
@Tag(name = "OmsOrderController", description = "Order management")
public class OmsOrderController {

    @Resource
    private OmsOrderService omsOrderService;

    @GetMapping("/detail/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','order:view')")
    @Operation(summary = "Get order details", description = "Retrieve complete order details including delivery and items")
    public CommonResult<OmsOrderVO> orderDetail(@PathVariable Long id) {
        return omsOrderService.orderDetail(id);
    }
}
