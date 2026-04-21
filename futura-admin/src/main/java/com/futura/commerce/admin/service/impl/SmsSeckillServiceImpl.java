package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.service.SmsSeckillService;
import com.futura.commerce.admin.vo.SmsFlashSaleVO;
import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.PmsProductSku;
import com.futura.commerce.mbg.model.SmsActivity;
import com.futura.commerce.mbg.model.SmsSeckill;
import com.futura.commerce.mbg.repository.PmsProductSkuRepository;
import com.futura.commerce.mbg.repository.SmsActivityRepository;
import com.futura.commerce.mbg.repository.SmsSeckillRepository;
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
 * Service implementation for flash sale promotions
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class SmsSeckillServiceImpl implements SmsSeckillService {

    @Resource
    private SmsSeckillRepository seckillRepository;

    @Resource
    private SmsActivityRepository activityRepository;

    @Resource
    private PmsProductSkuRepository productSkuRepository;

    @Override
    public CommonResult<Page<SmsFlashSaleVO>> getSkillList(Integer pageNum, Integer pageSize, Integer type) {
        int page = (pageNum != null && pageNum > 0) ? pageNum - 1 : 0;
        int size = (pageSize != null && pageSize > 0) ? pageSize : 10;
        Pageable pageable = PageRequest.of(page, size);

        List<SmsActivity> activities = activityRepository.findAll().stream()
                .filter(a -> type == null || Objects.equals(a.getType(), type))
                .collect(Collectors.toList());

        if (activities.isEmpty()) {
            return CommonResult.success(new PageImpl<>(List.of(), pageable, 0), "No flash sale activities found");
        }

        Map<Long, SmsActivity> activityMap = activities.stream()
                .collect(Collectors.toMap(SmsActivity::getId, a -> a, (k1, k2) -> k1));

        List<SmsSeckill> seckills = seckillRepository.findAll().stream()
                .filter(s -> s.getActivityId() != null && activityMap.containsKey(s.getActivityId()))
                .collect(Collectors.toList());

        if (seckills.isEmpty()) {
            return CommonResult.success(new PageImpl<>(List.of(), pageable, 0), "No flash sale items found");
        }

        List<Long> skuIds = seckills.stream().map(SmsSeckill::getSkuId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, PmsProductSku> skuMap = productSkuRepository.findAllById(skuIds).stream()
                .collect(Collectors.toMap(PmsProductSku::getId, s -> s, (k1, k2) -> k1));

        List<SmsFlashSaleVO> voList = seckills.stream().map(s -> {
            SmsActivity activity = activityMap.get(s.getActivityId());
            PmsProductSku sku = skuMap.get(s.getSkuId());

            SmsFlashSaleVO vo = new SmsFlashSaleVO();
            vo.setId(s.getId());
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

            if (activity != null) {
                vo.setStartTime(activity.getStartTime());
                vo.setEndTime(activity.getEndTime());
            }

            return vo;
        }).collect(Collectors.toList());

        int total = voList.size();
        int fromIndex = Math.min(page * size, total);
        int toIndex = Math.min(fromIndex + size, total);
        List<SmsFlashSaleVO> pageContent = voList.subList(fromIndex, toIndex);

        return CommonResult.success(new PageImpl<>(pageContent, pageable, total), "Fetched flash sale list successfully");
    }
}
