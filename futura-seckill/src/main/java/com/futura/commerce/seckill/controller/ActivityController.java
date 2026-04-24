package com.futura.commerce.seckill.controller;

import com.futura.commerce.seckill.dto.ActivitySearchDTO;
import com.futura.commerce.seckill.service.SmsActivityService;
import com.futura.commerce.seckill.vo.ActivityListVO;
import com.futura.commerce.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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

    /**
     * Marketing activity list across all promotional types
     */
    @RequestMapping("/list")
    @Operation(summary = "Activity list", description = "Retrieve promotional activities across types (seckill, coupon, follow discount, full reduction)")
    public CommonResult<Page<ActivityListVO>> activityList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer type) {
        return smsActivityService.getActivityList(pageNum, pageSize, type);
    }

    @org.springframework.web.bind.annotation.PostMapping("/save")
    @Operation(summary = "Create or update activity", description = "Save promotional campaign")
    public CommonResult<com.futura.commerce.mbg.model.SmsActivity> saveActivity(
            @org.springframework.web.bind.annotation.RequestBody com.futura.commerce.mbg.model.SmsActivity activity) {
        return smsActivityService.createActivity(activity);
    }

    @RequestMapping("/smsStatus")
    @Operation(summary = "Generic activity online/offline status", description = "Toggle generic activity online or offline status")
    public CommonResult<Integer> activityStatus(@RequestParam Long id, @RequestParam(value = "smsStatus") Integer status) {
        return smsActivityService.activityStatus(id, status);
    }

    @RequestMapping("/search")
    @Operation(summary = "Generic activity search", description = "Search promotional activities with filter criteria")
    public CommonResult<Page<ActivitySearchDTO>> activitySearch(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @org.springframework.web.bind.annotation.RequestBody ActivitySearchDTO activitySearchDTO) {
        return smsActivityService.activitySearch(pageNum, pageSize, activitySearchDTO);
    }
}
