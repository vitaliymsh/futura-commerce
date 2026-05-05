package com.futura.commerce.seckill.service;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.MyCoupon;

import java.util.List;
import java.util.Map;

/**
 * Service interface for customer coupon operations
 *
 * @author Vitalii
 */
public interface MyCouponService {

    CommonResult<Object> receiveCoupon(Long userId, Integer type, Long couponId);

    CommonResult<List<MyCoupon>> myCouponList(Long userId, Integer pageNum, Integer pageSize, Integer type);

    CommonResult<Map<String, Object>> myCouponCountByType(Long userId);
}
