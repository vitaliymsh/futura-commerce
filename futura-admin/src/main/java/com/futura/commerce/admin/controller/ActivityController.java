package com.futura.commerce.admin.controller;

import com.futura.commerce.admin.service.SmsActivityService;
import com.futura.commerce.admin.vo.ActivityListVO;
import com.futura.commerce.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Marketing activity controller
 *
 * @author Vitalii
 */
@Slf4j
@RestController
@RequestMapping("/activity")
@Tag(name = "ActivityController", description = "Marketing activity management")
public class ActivityController {

    @Resource
    private SmsActivityService smsActivityService;

    @RequestMapping("/list")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','activity:view','admin:promotion:view')")
    @Operation(summary = "Activity list", description = "Retrieve promotional activities across types (seckill, coupon, follow discount, full reduction)")
    public CommonResult<Page<ActivityListVO>> activityList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "type", required = false, defaultValue = "1") Integer type) {
        int actType = type != null ? type : 1;
        return smsActivityService.getActivityList(pageNum, pageSize, actType);
    }

    @RequestMapping("/smsStatus")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','activity:view')")
    @Operation(summary = "Generic activity online/offline status", description = "Toggle generic activity online or offline status")
    public CommonResult<Integer> activityStatus(@RequestParam Long id, @RequestParam(value = "smsStatus") Integer status) {
        return smsActivityService.activityStatus(id, status);
    }

    @RequestMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','activity:view')")
    @Operation(summary = "Generic activity search", description = "Search promotional activities with filter criteria")
    public CommonResult<Page<com.futura.commerce.admin.dto.ActivitySearchDTO>> activitySearch(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @org.springframework.web.bind.annotation.RequestBody com.futura.commerce.admin.dto.ActivitySearchDTO activitySearchDTO) {
        return smsActivityService.activitySearch(pageNum, pageSize, activitySearchDTO);
    }
}
