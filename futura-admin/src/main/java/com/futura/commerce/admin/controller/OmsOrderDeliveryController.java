package com.futura.commerce.admin.controller;

import com.futura.commerce.admin.service.OmsOrderDeliveryService;
import com.futura.commerce.admin.vo.OmsOrderAndDeliveryVO;
import com.futura.commerce.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Order delivery and logistics controller
 *
 * @author Vitalii
 */
@Slf4j
@RestController
@RequestMapping("/delivery")
@Tag(name = "DeliveryController", description = "Order delivery and logistics management")
public class OmsOrderDeliveryController {

    @Resource
    private OmsOrderDeliveryService deliveryService;

    /**
     * Get order delivery list
     */
    @RequestMapping("/list")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','delivery:view')")
    @Operation(summary = "Order delivery list", description = "Retrieve list of all order delivery records")
    public CommonResult<List<OmsOrderAndDeliveryVO>> list() {
        return deliveryService.selcetOrderAndDeliveryList();
    }

    /**
     * Get logistics details
     */
    @GetMapping("/logistics/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','delivery:view')")
    @Operation(summary = "Get logistics details", description = "Retrieve delivery record and transit traces by ID")
    public CommonResult<?> getLogistics(@PathVariable Long id) {
        return deliveryService.getLogisticsById(id);
    }

    /**
     * Search logistics
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','delivery:view')")
    @Operation(summary = "Search delivery info", description = "Filter logistics records by order number and status")
    public CommonResult<?> search(
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer deliveryStatus) {
        return deliveryService.search(orderNo, deliveryStatus);
    }
}
