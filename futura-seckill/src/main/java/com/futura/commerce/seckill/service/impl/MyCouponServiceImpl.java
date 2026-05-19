package com.futura.commerce.seckill.service.impl;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.MyCoupon;
import com.futura.commerce.mbg.repository.MyCouponRepository;
import com.futura.commerce.seckill.service.MyCouponService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service implementation for customer coupon operations
 *
 * @author Vitalii
 */
@Service
public class MyCouponServiceImpl implements MyCouponService {

    @Resource
    private MyCouponRepository myCouponRepository;

    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult<Object> receiveCoupon(Long userId, Integer type, Long couponId) {
        if (userId == null || couponId == null) {
            return CommonResult.failed("Invalid user or coupon ID");
        }
        MyCoupon myCoupon = new MyCoupon();
        myCoupon.setUserId(userId);
        myCoupon.setCouponId(couponId);
        myCoupon.setCouponType(type != null ? Long.valueOf(type) : 1L);
        myCoupon.setStatus(0);
        myCoupon.setReceiveTime(LocalDateTime.now());
        myCouponRepository.save(myCoupon);
        return CommonResult.success(myCoupon, "Coupon received successfully");
    }

    @Override
    public CommonResult<List<MyCoupon>> myCouponList(Long userId, Integer pageNum, Integer pageSize, Integer type) {
        if (userId == null) {
            return CommonResult.unauthorized("Authentication required");
        }
        List<MyCoupon> coupons = myCouponRepository.findByUserId(userId);
        if (type != null) {
            coupons = coupons.stream()
                    .filter(c -> c.getStatus() != null && c.getStatus().equals(type))
                    .collect(Collectors.toList());
        }
        return CommonResult.success(coupons, "Fetched user coupons successfully");
    }

    @Override
    public CommonResult<Map<String, Object>> myCouponCountByType(Long userId) {
        if (userId == null) {
            return CommonResult.unauthorized("Authentication required");
        }
        List<MyCoupon> coupons = myCouponRepository.findByUserId(userId);
        long unused = coupons.stream().filter(c -> c.getStatus() != null && c.getStatus() == 0).count();
        long used = coupons.stream().filter(c -> c.getStatus() != null && c.getStatus() == 1).count();
        long expired = coupons.stream().filter(c -> c.getStatus() != null && c.getStatus() == 2).count();

        Map<String, Object> map = new HashMap<>();
        map.put("unused", unused);
        map.put("used", used);
        map.put("expired", expired);
        map.put("total", coupons.size());
        return CommonResult.success(map, "Fetched coupon counts successfully");
    }
}
