package com.futura.commerce.admin.controller;

import com.futura.commerce.admin.dto.DeliveryShipDTO;
import com.futura.commerce.admin.dto.OmsOrderDeliveryCancelDTO;
import com.futura.commerce.admin.dto.OmsOrderDeliverySearchDTO;
import com.futura.commerce.admin.dto.UpdateTrackingNoDTO;
import com.futura.commerce.admin.service.OmsOrderDeliveryService;
import com.futura.commerce.admin.vo.OmsDeliveryAndTraceVO;
import com.futura.commerce.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','delivery:view')")
    @Operation(summary = "Order delivery list", description = "Retrieve list of all order delivery records")
    public CommonResult<Page<OmsDeliveryAndTraceVO>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer[] deliveryStatus) {
        return deliveryService.selectOrderAndDeliveryList(page, pageSize, deliveryStatus);
    }

    /**
     * Search delivery records
     */
    @PostMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','delivery:view')")
    @Operation(summary = "Search delivery info", description = "Filter logistics records by criteria")
    public CommonResult<?> search(@RequestBody OmsOrderDeliverySearchDTO dto) {
        return deliveryService.search(dto);
    }

    /**
     * Get delivery companies list
     */
    @GetMapping("/company/list")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','delivery:view')")
    @Operation(summary = "Get delivery company list", description = "Retrieve active carrier list")
    public CommonResult<?> getDeliveryCompanyList() {
        return deliveryService.getDeliveryCompanyList();
    }

    /**
     * View delivery status statistics
     */
    @GetMapping("/status")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','delivery:view')")
    @Operation(summary = "Get delivery statuses", description = "Retrieve delivery status distribution counts")
    public CommonResult<Map<String, Long>> selectStatus() {
        return deliveryService.selectStatus();
    }

    /**
     * Ship order
     */
    @PostMapping("/ship/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','delivery:view')")
    @Operation(summary = "Ship order", description = "Dispatch order with shipping and carrier details")
    public CommonResult<DeliveryShipDTO> ship(@PathVariable(value = "id") Long orderId, @RequestBody DeliveryShipDTO dto) {
        return deliveryService.ship(orderId, dto);
    }

    /**
     * Update tracking number
     */
    @PutMapping("/tracking/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','delivery:view')")
    @Operation(summary = "Update tracking number", description = "Update tracking number for order delivery")
    public CommonResult<?> updateTrackingNo(@PathVariable(value = "id") Long orderId, @RequestBody UpdateTrackingNoDTO dto) {
        return deliveryService.updateTrackingNo(orderId, dto);
    }

    /**
     * Cancel delivery shipment
     */
    @PostMapping("/cancel/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','delivery:view')")
    @Operation(summary = "Cancel shipment", description = "Cancel pending shipment and soft-delete record")
    public CommonResult<?> cancelDelivery(@PathVariable(value = "id") Long orderId, @RequestBody OmsOrderDeliveryCancelDTO dto) {
        return deliveryService.cancelDelivery(orderId, dto);
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
}
