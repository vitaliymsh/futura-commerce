package com.futura.commerce.order.controller;

import com.futura.commerce.order.dto.DeliveryShipDTO;
import com.futura.commerce.order.dto.OmsOrderDeliveryCancelDTO;
import com.futura.commerce.order.dto.OmsOrderDeliverySearchDTO;
import com.futura.commerce.order.dto.UpdateTrackingNoDTO;
import com.futura.commerce.order.service.OmsOrderDeliveryService;
import com.futura.commerce.order.vo.OmsDeliveryAndTraceVO;
import com.futura.commerce.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    @Operation(summary = "Order delivery list", description = "Retrieve list of all order delivery records")
    public CommonResult<Page<OmsDeliveryAndTraceVO>> list(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageNum", required = false) Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer[] deliveryStatus) {
        int p = page != null ? page : (pageNum != null ? pageNum : 1);
        return deliveryService.selectOrderAndDeliveryList(p, pageSize, deliveryStatus);
    }

    /**
     * Search delivery records
     */
    @PostMapping("/search")
    @Operation(summary = "Search delivery info", description = "Filter logistics records by criteria")
    public CommonResult<?> search(@RequestBody OmsOrderDeliverySearchDTO dto) {
        return deliveryService.search(dto);
    }

    /**
     * Get delivery companies list
     */
    @GetMapping("/company/list")
    @Operation(summary = "Get delivery company list", description = "Retrieve active carrier list")
    public CommonResult<?> getDeliveryCompanyList() {
        return deliveryService.getDeliveryCompanyList();
    }

    /**
     * View delivery status statistics
     */
    @GetMapping("/status")
    @Operation(summary = "Get delivery statuses", description = "Retrieve delivery status distribution counts")
    public CommonResult<Map<String, Long>> selectStatus() {
        return deliveryService.selectStatus();
    }

    /**
     * Ship order
     */
    @PostMapping("/ship/{id}")
    @Operation(summary = "Ship order", description = "Dispatch order with shipping and carrier details")
    public CommonResult<DeliveryShipDTO> ship(@PathVariable(value = "id") Long orderId, @RequestBody DeliveryShipDTO dto) {
        return deliveryService.ship(orderId, dto);
    }

    /**
     * Update tracking number
     */
    @PutMapping("/tracking/{id}")
    @Operation(summary = "Update tracking number", description = "Update tracking number for order delivery")
    public CommonResult<?> updateTrackingNo(@PathVariable(value = "id") Long orderId, @RequestBody UpdateTrackingNoDTO dto) {
        return deliveryService.updateTrackingNo(orderId, dto);
    }

    /**
     * Cancel delivery shipment
     */
    @PostMapping("/cancel/{id}")
    @Operation(summary = "Cancel shipment", description = "Cancel pending shipment and soft-delete record")
    public CommonResult<?> cancelDelivery(@PathVariable(value = "id") Long orderId, @RequestBody OmsOrderDeliveryCancelDTO dto) {
        return deliveryService.cancelDelivery(orderId, dto);
    }

    /**
     * Get logistics details
     */
    @GetMapping("/logistics/{id}")
    @Operation(summary = "Get logistics details", description = "Retrieve delivery record and transit traces by ID")
    public CommonResult<?> getLogistics(@PathVariable Long id) {
        return deliveryService.getLogisticsById(id);
    }
}
