package com.futura.commerce.admin.controller;

import com.futura.commerce.admin.service.OmsOrderItemService;
import com.futura.commerce.admin.vo.OmsOrderItemVO;
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
 * Order item statistical analytics controller
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
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','promotion:view')")
    public CommonResult<List<OmsOrderItemVO>> itemList() {
        return omsOrderItemService.itemList();
    }
}
