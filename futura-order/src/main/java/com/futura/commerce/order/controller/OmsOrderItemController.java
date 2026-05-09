package com.futura.commerce.order.controller;

import com.futura.commerce.common.dto.AiOrderProductDto;
import com.futura.commerce.order.service.OmsOrderItemService;
import com.futura.commerce.order.vo.OmsOrderItemVO;
import com.futura.commerce.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Order item statistical analytics and internal lookup controller
 *
 * @author Vitalii
 */
@Slf4j
@RestController
@RequestMapping("/item")
@Tag(name = "OrderItemController", description = "Order item analysis and metrics")
public class OmsOrderItemController {

    @Resource
    private OmsOrderItemService omsOrderItemService;

    @GetMapping("/list")
    @Operation(summary = "Order item analytics list", description = "Product purchase frequency, demographics and metrics")
    public CommonResult<List<OmsOrderItemVO>> itemList() {
        return omsOrderItemService.itemList();
    }

    /**
     * Internal RPC endpoint for AI module: fetch order and product SKU details by order number
     */
    @GetMapping("/getByOrderNo")
    @Operation(summary = "Get order and product info by order number", description = "Internal service lookup for AI tool invocation")
    public AiOrderProductDto getByOrderNo(@RequestParam("orderNo") String orderNo,
                                          @RequestParam(value = "memoryId", required = false) Integer memoryId) {
        return omsOrderItemService.getOrderAndProductByOrderNo(orderNo);
    }
}
