package com.futura.commerce.admin.controller;

import com.futura.commerce.admin.service.OmsOrderDeliveryTraceService;
import com.futura.commerce.admin.vo.OmsDeliveryVO;
import com.futura.commerce.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','delivery:view')")
    public CommonResult<List<OmsDeliveryVO>> list() {
        List<OmsDeliveryVO> list = omsOrderDeliveryTraceService.getDeliveryList();
        return CommonResult.success(list, "Fetched delivery trace list successfully");
    }
}
