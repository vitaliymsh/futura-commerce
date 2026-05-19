package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.service.PmsDataStatService;
import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.PmsDataStat;
import com.futura.commerce.mbg.repository.PmsDataStatRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Service implementation for platform statistics and dashboard analytics
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class PmsDataStatServiceImpl implements PmsDataStatService {

    @Resource
    private PmsDataStatRepository pmsDataStatRepository;

    @Override
    public List<PmsDataStat> findAll() {
        return pmsDataStatRepository.findAll();
    }

    @Override
    public Optional<PmsDataStat> findById(Long id) {
        return pmsDataStatRepository.findById(id);
    }

    @Override
    public PmsDataStat save(PmsDataStat entity) {
        return pmsDataStatRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        pmsDataStatRepository.deleteById(id);
    }

    @Override
    public CommonResult<Map<String, Object>> selectUserDashboard(String startDate, String endDate) {
        try {
            List<PmsDataStat> statList;
            Map<String, Object> result = new HashMap<>();

            // 1. If date range is omitted, query today's metrics
            if (startDate == null || endDate == null || startDate.isBlank() || endDate.isBlank()) {
                LocalDate today = LocalDate.now();
                statList = pmsDataStatRepository.findByStatDate(today);
                result.put("statList", statList);
            } else {
                LocalDate start = LocalDate.parse(startDate);
                LocalDate end = LocalDate.parse(endDate);
                log.info("Querying sales statistics between {} and {}", start, end);
                statList = pmsDataStatRepository.findByStatDateBetweenOrderByStatDateAsc(start, end);

                int totalOrderCount = 0;
                BigDecimal totalAmount = BigDecimal.ZERO;
                int totalUserCount = 0;
                int totalProductCount = 0;

                for (PmsDataStat stat : statList) {
                    totalOrderCount += stat.getOrderCount() != null ? stat.getOrderCount() : 0;
                    if (stat.getSalesAmount() != null) {
                        totalAmount = totalAmount.add(stat.getSalesAmount());
                    }
                    totalUserCount += stat.getUserCount() != null ? stat.getUserCount() : 0;
                    totalProductCount += stat.getProductCount() != null ? stat.getProductCount() : 0;
                }

                result.put("statList", statList);
                result.put("totalOrderCount", totalOrderCount);
                result.put("totalAmount", totalAmount);
                result.put("totalUserCount", totalUserCount);
                result.put("totalProductCount", totalProductCount);
            }

            return CommonResult.success(result, "Dashboard statistics retrieved successfully");
        } catch (Exception e) {
            log.error("Failed to query dashboard statistics", e);
            return CommonResult.failed("Failed to query statistics: " + e.getMessage());
        }
    }
}
