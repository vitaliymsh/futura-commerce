package com.futura.commerce.user.controller;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.feign.seckill.SeckillFeignClient;
import com.futura.commerce.user.service.impl.UserSeckillServer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * Customer promotional activity controller
 *
 * @author Vitalii
 */
@RestController
@RequestMapping("/user/activity")
@Tag(name = "UserActivityController", description = "Customer marketing promotions and activity listings")
public class UserActivityController {

    @Resource
    private SeckillFeignClient seckillFeignClient;

    @Resource
    private UserSeckillServer userSeckillServer;

    /**
     * Flash sale activity list (type=1)
     */
    @GetMapping("/list")
    @Operation(summary = "Flash sale activity list", description = "Paginated flash sale activity listings")
    public CommonResult<?> list(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "activityType", required = false) Integer activityType,
            @RequestParam(value = "type", required = false) Integer type) {
        int actType = activityType != null ? activityType : (type != null ? type : 1);
        return seckillFeignClient.activityList(pageNum, pageSize, actType);
    }

    /**
     * Promotional campaigns list (coupons, instant discounts, full reduction)
     */
    @GetMapping("/lists")
    @Operation(summary = "Promotional campaign list", description = "Unified coupons, discounts, and reduction activities")
    public CommonResult<?> lists(@RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize) {
        return userSeckillServer.activityList(pageNum, pageSize);
    }

    /**
     * Fetch promotional campaign by type
     */
    @GetMapping("/type/{type}")
    @Operation(summary = "Query activities by type", description = "Filter activities: 2-coupons, 3-discount, 4-reduction")
    public CommonResult<?> listByType(@PathVariable Integer type,
                                      @RequestParam Integer pageNum,
                                      @RequestParam Integer pageSize) {
        return seckillFeignClient.activityList(pageNum, pageSize, type);
    }
}
