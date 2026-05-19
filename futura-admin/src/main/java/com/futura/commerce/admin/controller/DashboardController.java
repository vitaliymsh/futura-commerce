package com.futura.commerce.admin.controller;

import com.futura.commerce.admin.service.PmsDataStatService;
import com.futura.commerce.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Dashboard and analytics controller
 *
 * @author Vitalii
 */
@Slf4j
@RestController
@RequestMapping("/dashboard")
@Tag(name = "DashboardController", description = "Dashboard analytics and metrics")
public class DashboardController {

    @Resource
    private PmsDataStatService pmsDataStatService;

    @GetMapping("/stats")
    @Operation(summary = "Retrieve sales and performance metrics by date range")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','dashboard:view','admin:dashboard:view')")
    public CommonResult<Map<String, Object>> stat(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {
        return pmsDataStatService.selectUserDashboard(startDate, endDate);
    }
}
