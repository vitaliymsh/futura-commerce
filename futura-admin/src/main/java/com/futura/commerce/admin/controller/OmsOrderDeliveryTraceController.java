package com.futura.commerce.admin.controller;

import com.futura.commerce.admin.dto.OmsOrderDeliveryTraceSearchDTO;
import com.futura.commerce.admin.dto.OmsOrderDeliveryUpdateDTO;
import com.futura.commerce.admin.service.OmsOrderDeliveryTraceService;
import com.futura.commerce.admin.vo.OmsDeliveryAndTraceVO;
import com.futura.commerce.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Order delivery trace tracking controller
 *
 * @author Vitalii
 */
@Slf4j
@RestController
@RequestMapping("/deliveryTrace")
@Tag(name = "DeliveryTraceController", description = "Order delivery trace management")
public class OmsOrderDeliveryTraceController {

    @Resource
    private OmsOrderDeliveryTraceService omsOrderDeliveryTraceService;

    /**
     * Get logistics delivery trace list
     */
    @Operation(summary = "Get delivery trace list", description = "Retrieve list of all deliveries and tracking info")
    @GetMapping("/list")
    public CommonResult<List<OmsDeliveryAndTraceVO>> list() {
        List<OmsDeliveryAndTraceVO> list = omsOrderDeliveryTraceService.getDeliveryList();
        return CommonResult.success(list, "Fetched delivery trace list successfully");
    }

    /**
     * Get logistics trace details
     */
    @GetMapping("/logistics/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','delivery:view')")
    @Operation(summary = "Get logistics trace", description = "Get logistics and trace checkpoints by delivery ID")
    public CommonResult<Map<String, Object>> getLogistics(@PathVariable Long id) {
        return omsOrderDeliveryTraceService.getLogisticsById(id);
    }

    /**
     * Update order delivery logistics information
     */
    @Operation(summary = "Update order delivery info", description = "Update delivery courier, number, and receiver details")
    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','delivery:edit')")
    public CommonResult<?> update(@RequestBody OmsOrderDeliveryUpdateDTO dto) {
        return omsOrderDeliveryTraceService.updateDeliveryTraceInfo(dto);
    }

    /**
     * Search delivery trace information
     */
    @PostMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','delivery:view')")
    @Operation(summary = "Search logistics info", description = "Search logistics information by criteria")
    public CommonResult<List<OmsDeliveryAndTraceVO>> search(@RequestBody OmsOrderDeliveryTraceSearchDTO dto) {
        return omsOrderDeliveryTraceService.searchDto(dto);
    }
}
