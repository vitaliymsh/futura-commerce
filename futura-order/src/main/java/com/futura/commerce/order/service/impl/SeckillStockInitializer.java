package com.futura.commerce.order.service.impl;

import com.futura.commerce.mbg.model.SmsSeckill;
import com.futura.commerce.mbg.repository.SmsSeckillRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Flash sale inventory warm-up runner on application startup
 *
 * @author Vitalii
 */
@Slf4j
@Component
public class SeckillStockInitializer implements CommandLineRunner {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private SmsSeckillRepository smsSeckillRepository;

    @Value("${futura.seckill.default-sku-id:296}")
    private long defaultSkuId;

    @Override
    public void run(String... args) {
        String key = "seckill:stock:" + defaultSkuId;

        Boolean hasKey = stringRedisTemplate.hasKey(key);
        if (Boolean.TRUE.equals(hasKey)) {
            log.info("Redis already has warmed up flash sale stock for skuId: {}", defaultSkuId);
            return;
        }

        List<SmsSeckill> seckills = smsSeckillRepository.findAll();
        SmsSeckill target = seckills.stream()
                .filter(s -> s.getSkuId() != null && s.getSkuId().equals(defaultSkuId))
                .findFirst()
                .orElse(null);

        if (target == null) {
            log.warn("No flash sale promotion record found in database for skuId: {}, skipping Redis warm-up", defaultSkuId);
            return;
        }

        int dbStock = target.getStock() != null ? target.getStock() : 0;
        if (dbStock <= 0) {
            log.warn("Database flash sale stock for skuId: {} is {}, replenishment required", defaultSkuId, dbStock);
            return;
        }

        stringRedisTemplate.opsForValue().set(key, String.valueOf(dbStock), 24, TimeUnit.HOURS);
        log.info("Successfully warmed up flash sale stock to Redis from database: skuId={}, stock={}", defaultSkuId, dbStock);
    }
}
