package com.futura.commerce.order.controller;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.order.dto.PayDTO;
import com.futura.commerce.order.service.OmsOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Customer order payment controller
 *
 * @author Vitalii
 */
@RestController
@RequestMapping("/user/order")
@Tag(name = "UserOrderPayController", description = "Customer order payment processing")
public class UserOrderPayController {

    @Resource
    private OmsOrderService orderService;

    @PostMapping("/pay")
    @Operation(summary = "Order payment", description = "Process order flash sale purchase and payment")
    public CommonResult<?> orderPay(@RequestBody PayDTO payDTO) {
        return orderService.orderPay(payDTO);
    }
}
