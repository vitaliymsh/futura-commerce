package com.futura.commerce.user.service.impl;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.feign.seckill.SeckillFeignClient;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Aggregator component querying marketing promotion activities for customer storefront
 *
 * @author Vitalii
 */
@Slf4j
@Component
public class UserSeckillServer {

    @Resource
    private SeckillFeignClient seckillFeignClient;

    public CommonResult<?> activityList(Integer pageNum, Integer pageSize) {
        Map<String, Object> result = new HashMap<>();
        try {
            CommonResult<?> couponList = seckillFeignClient.activityList(pageNum, pageSize, 2);
            result.put("couponList", couponList != null ? couponList.getData() : null);

            CommonResult<?> discountList = seckillFeignClient.activityList(pageNum, pageSize, 3);
            result.put("discountList", discountList != null ? discountList.getData() : null);

            CommonResult<?> fullReductionList = seckillFeignClient.activityList(pageNum, pageSize, 4);
            result.put("fullReductionList", fullReductionList != null ? fullReductionList.getData() : null);
        } catch (Exception e) {
            log.error("Failed to query activity data from seckill service", e);
            return CommonResult.failed("Failed to retrieve marketing activity data");
        }
        return CommonResult.success(result, "Activity list retrieved successfully");
    }
}
