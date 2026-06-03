package com.futura.commerce.order.controller;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.order.dto.AfterOrderDTO;
import com.futura.commerce.order.service.OmsOrderAfterSalesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Order after-sales management controller
 *
 * @author Vitalii
 */
@Slf4j
@RestController
@RequestMapping("/after")
@Tag(name = "OmsOrderAfterController", description = "Order after-sales customer service")
public class OmsOrderAfterController {

    @Resource
    private OmsOrderAfterSalesService omsOrderAfterSalesService;

    @GetMapping("/list")
    @Operation(summary = "After-sales request list", description = "Query paginated list of order after-sales applications")
    public CommonResult<Page<AfterOrderDTO>> orderAfterList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return omsOrderAfterSalesService.orderAfterList(page, pageSize);
    }
}
