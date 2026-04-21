package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.service.SmsActivityService;
import com.futura.commerce.admin.vo.ActivityListVO;
import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.*;
import com.futura.commerce.mbg.repository.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementation for managing SmsActivity
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class SmsActivityServiceImpl implements SmsActivityService {

    @Resource
    private SmsActivityRepository smsActivityRepository;

    @Resource
    private SmsSeckillRepository smsSeckillRepository;

    @Resource
    private SmsCouponRepository smsCouponRepository;

    @Resource
    private SmsFollowDiscountRepository smsFollowDiscountRepository;

    @Resource
    private SmsFullReductionRepository smsFullReductionRepository;

    @Resource
    private PmsProductSkuRepository productSkuRepository;

    @Override
    public List<SmsActivity> findAll() {
        return smsActivityRepository.findAll();
    }

    @Override
    public Optional<SmsActivity> findById(Long id) {
        return smsActivityRepository.findById(id);
    }

    @Override
    public SmsActivity save(SmsActivity entity) {
        return smsActivityRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        smsActivityRepository.deleteById(id);
    }

    @Override
    public CommonResult<Page<ActivityListVO>> getActivityList(Integer pageNum, Integer pageSize, Integer type) {
        int page = (pageNum != null && pageNum > 0) ? pageNum - 1 : 0;
        int size = (pageSize != null && pageSize > 0) ? pageSize : 10;
        Pageable pageable = PageRequest.of(page, size);

        if (type == null) {
            return CommonResult.failed("Activity type is required");
        }

        List<ActivityListVO> voList;
        switch (type) {
            case 1 -> voList = getSeckillList(type);
            case 2 -> voList = getCouponList(type);
            case 3 -> voList = getFollowDiscountList(type);
            case 4 -> voList = getFullReductionList(type);
            default -> {
                return CommonResult.failed("Invalid activity type");
            }
        }

        int total = voList.size();
        int fromIndex = Math.min(page * size, total);
        int toIndex = Math.min(fromIndex + size, total);
        List<ActivityListVO> pageContent = voList.subList(fromIndex, toIndex);

        return CommonResult.success(new PageImpl<>(pageContent, pageable, total), "Query activity list successful");
    }

    private List<ActivityListVO> getSeckillList(Integer type) {
        List<SmsActivity> activities = smsActivityRepository.findAll().stream()
                .filter(a -> Objects.equals(a.getType(), type))
                .collect(Collectors.toList());
        if (activities.isEmpty()) return List.of();

        Map<Long, SmsActivity> actMap = activities.stream().collect(Collectors.toMap(SmsActivity::getId, a -> a, (k1, k2) -> k1));
        List<SmsSeckill> seckills = smsSeckillRepository.findAll().stream()
                .filter(s -> s.getActivityId() != null && actMap.containsKey(s.getActivityId()))
                .collect(Collectors.toList());
        if (seckills.isEmpty()) return List.of();

        List<Long> skuIds = seckills.stream().map(SmsSeckill::getSkuId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, PmsProductSku> skuMap = productSkuRepository.findAllById(skuIds).stream()
                .collect(Collectors.toMap(PmsProductSku::getId, s -> s, (k1, k2) -> k1));

        return seckills.stream().map(s -> {
            SmsActivity a = actMap.get(s.getActivityId());
            PmsProductSku sku = skuMap.get(s.getSkuId());

            ActivityListVO vo = new ActivityListVO();
            vo.setId(s.getId());
            vo.setActivityMainId(a.getId());
            vo.setSkuId(s.getSkuId());
            vo.setSeckillPrice(s.getSeckillPrice());
            vo.setStock(s.getStock());
            vo.setSoldStock(s.getSoldStock());
            vo.setLimitQuantity(s.getLimitQuantity());
            vo.setStockStatus(s.getStockStatus());
            if (sku != null) {
                vo.setOriginalPrice(sku.getPrice());
                vo.setProductName(sku.getModel());
                vo.setPic(sku.getPic());
            }
            vo.setStartTime(a.getStartTime());
            vo.setEndTime(a.getEndTime());
            vo.setActivityName(a.getTitle());
            vo.setStatus(a.getStatus());
            vo.setSmsStatus(a.getSmsStatus() != null ? Integer.valueOf(a.getSmsStatus()) : null);
            return vo;
        }).collect(Collectors.toList());
    }

    private List<ActivityListVO> getCouponList(Integer type) {
        List<SmsActivity> activities = smsActivityRepository.findAll().stream()
                .filter(a -> Objects.equals(a.getType(), type))
                .collect(Collectors.toList());
        if (activities.isEmpty()) return List.of();

        Map<Long, SmsActivity> actMap = activities.stream().collect(Collectors.toMap(SmsActivity::getId, a -> a, (k1, k2) -> k1));
        List<SmsCoupon> coupons = smsCouponRepository.findAll().stream()
                .filter(c -> c.getActivityId() != null && actMap.containsKey(c.getActivityId()))
                .collect(Collectors.toList());

        return coupons.stream().map(c -> {
            SmsActivity a = actMap.get(c.getActivityId());
            ActivityListVO vo = new ActivityListVO();
            vo.setId(c.getId());
            vo.setActivityMainId(a.getId());
            vo.setCouponType(c.getCouponType());
            vo.setDiscountValue(c.getDiscountValue());
            vo.setMinConsume(c.getMinConsume());
            vo.setUseScope(c.getUseScope());
            vo.setTotalCount(c.getTotalCount());
            vo.setReceivedCount(c.getReceivedCount());
            vo.setUsedCount(c.getUsedCount());
            vo.setLimitPerUser(c.getLimitPerUser());
            vo.setStartTime(a.getStartTime());
            vo.setEndTime(a.getEndTime());
            vo.setActivityName(a.getTitle());
            vo.setStatus(a.getStatus());
            vo.setSmsStatus(a.getSmsStatus() != null ? Integer.valueOf(a.getSmsStatus()) : null);
            return vo;
        }).collect(Collectors.toList());
    }

    private List<ActivityListVO> getFollowDiscountList(Integer type) {
        List<SmsActivity> activities = smsActivityRepository.findAll().stream()
                .filter(a -> Objects.equals(a.getType(), type))
                .collect(Collectors.toList());
        if (activities.isEmpty()) return List.of();

        Map<Long, SmsActivity> actMap = activities.stream().collect(Collectors.toMap(SmsActivity::getId, a -> a, (k1, k2) -> k1));
        List<SmsFollowDiscount> discounts = smsFollowDiscountRepository.findAll().stream()
                .filter(f -> f.getActivityId() != null && actMap.containsKey(f.getActivityId()))
                .collect(Collectors.toList());

        return discounts.stream().map(f -> {
            SmsActivity a = actMap.get(f.getActivityId());
            ActivityListVO vo = new ActivityListVO();
            vo.setId(f.getId());
            vo.setActivityMainId(a.getId());
            vo.setDiscountAmount(f.getDiscountAmount());
            vo.setUseCondition(f.getUseCondition());
            vo.setUseScope(f.getUseScope());
            vo.setParticipantCount(f.getParticipantCount());
            vo.setUsedCount2(f.getUsedCount());
            vo.setTotalDiscountAmount(f.getTotalDiscountAmount());
            vo.setStartTime(a.getStartTime());
            vo.setEndTime(a.getEndTime());
            vo.setActivityName(a.getTitle());
            vo.setStatus(a.getStatus());
            vo.setSmsStatus(a.getSmsStatus() != null ? Integer.valueOf(a.getSmsStatus()) : null);
            return vo;
        }).collect(Collectors.toList());
    }

    private List<ActivityListVO> getFullReductionList(Integer type) {
        List<SmsActivity> activities = smsActivityRepository.findAll().stream()
                .filter(a -> Objects.equals(a.getType(), type))
                .collect(Collectors.toList());
        if (activities.isEmpty()) return List.of();

        Map<Long, SmsActivity> actMap = activities.stream().collect(Collectors.toMap(SmsActivity::getId, a -> a, (k1, k2) -> k1));
        List<SmsFullReduction> reductions = smsFullReductionRepository.findAll().stream()
                .filter(r -> r.getActivityId() != null && actMap.containsKey(r.getActivityId()))
                .collect(Collectors.toList());

        return reductions.stream().map(r -> {
            SmsActivity a = actMap.get(r.getActivityId());
            ActivityListVO vo = new ActivityListVO();
            vo.setId(r.getId());
            vo.setActivityMainId(a.getId());
            vo.setFullAmount(r.getFullAmount());
            vo.setReductionAmount(r.getReductionAmount());
            vo.setDiscountRate(r.getDiscountRate());
            vo.setRuleType(r.getRuleType());
            vo.setOverlayRule(r.getOverlayRule());
            vo.setUseScope(r.getUseScope());
            vo.setOrderCount(r.getOrderCount());
            vo.setTotalDiscountAmount2(r.getTotalDiscountAmount());
            vo.setStartTime(a.getStartTime());
            vo.setEndTime(a.getEndTime());
            vo.setActivityName(a.getTitle());
            vo.setStatus(a.getStatus());
            vo.setSmsStatus(a.getSmsStatus() != null ? Integer.valueOf(a.getSmsStatus()) : null);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public CommonResult<Integer> activityStatus(Long id, Integer status) {
        return null;
    }

    @Override
    public CommonResult<Page<ActivitySearchDTO>> activitySearch(Integer pageNum, Integer pageSize, ActivitySearchDTO activitySearchDTO) {
        return null;
    }
}
