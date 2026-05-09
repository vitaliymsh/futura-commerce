package com.futura.commerce.order.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.futura.commerce.mbg.model.OmsOrder;
import com.futura.commerce.mbg.repository.MyCouponRepository;
import com.futura.commerce.mbg.repository.OmsOrderRepository;
import com.futura.commerce.mbg.repository.SmsSeckillRepository;
import com.futura.commerce.order.config.RabbitMqConfig;
import com.futura.commerce.order.constant.RedisKeyConstant;
import com.futura.commerce.order.dto.PayDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static com.futura.commerce.order.constant.RedisKeyConstant.SECKILL_STOCK;
import static com.futura.commerce.order.constant.RedisKeyConstant.SECKILL_USER;

/**
 * RabbitMQ consumer for asynchronous seckill order placement and inventory deduction
 *
 * @author Vitalii
 */
@Slf4j
@Component
public class SeckillOrderMessageConsumer {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private SmsSeckillRepository smsSeckillRepository;

    @Resource
    private MyCouponRepository myCouponRepository;

    @Resource
    private OmsOrderRepository orderRepository;

    @Resource
    private ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMqConfig.SECKILL_PAY_QUEUE)
    @Transactional(rollbackFor = Exception.class)
    public void onMessage(String message) {
        PayDTO payDTO;
        try {
            payDTO = objectMapper.readValue(message, PayDTO.class);
        } catch (Exception e) {
            log.error("Failed to parse payDTO message: {}", message, e);
            return;
        }

        log.info("Received seckill order message: {}", payDTO);
        Long userId = 1106L;
        Long skuId = payDTO.getSkuId();

        String userKey = SECKILL_USER + userId + ":" + skuId;
        String stockKey = SECKILL_STOCK + skuId;
        String lockKey = RedisKeyConstant.SECKILL_LOCK_PREFIX + userId + ":" + skuId;

        if (payDTO.getProducts() == null || payDTO.getProducts().isEmpty()) {
            log.warn("Seckill payDTO has no product items: {}", payDTO);
            return;
        }

        PayDTO.ProductItemDTO product = payDTO.getProducts().get(0);
        Integer buyNum = product.getQuantity() != null ? product.getQuantity() : 1;

        // Distributed lock to prevent duplicate orders per user and SKU
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean acquired = lock.tryLock(3, 10, TimeUnit.SECONDS);
            if (!acquired) {
                compensateRedis(stockKey, userKey, buyNum);
                log.info("Duplicate order prevented for user {} sku {}", userId, skuId);
                return;
            }

            // Optimistic stock decrement in sms_seckill table
            int updatedRows = smsSeckillRepository.decrStock(skuId, buyNum);
            if (updatedRows <= 0) {
                compensateRedis(stockKey, userKey, buyNum);
                log.info("Insufficient stock for SKU {}", skuId);
                return;
            }

            // Create persistent order
            OmsOrder order = new OmsOrder();
            order.setOrderNo(product.getOrderNo());
            order.setRemark(product.getRemark());
            order.setPayAmount(product.getPayAmount());
            order.setTotalAmount(product.getTotalAmount());
            order.setUserId(userId);
            order.setStatus(1); // Status 1: paid successfully

            if (payDTO.getAddress() != null) {
                order.setReceiverAddress(
                        (payDTO.getAddress().getProvince() != null ? payDTO.getAddress().getProvince() : "") +
                        (payDTO.getAddress().getCity() != null ? payDTO.getAddress().getCity() : "") +
                        (payDTO.getAddress().getDistrict() != null ? payDTO.getAddress().getDistrict() : "") +
                        (payDTO.getAddress().getDetail() != null ? payDTO.getAddress().getDetail() : "")
                );
                order.setReceiverPhone(payDTO.getAddress().getPhone());
                order.setReceiverName(payDTO.getAddress().getName());
            }
            order.setPayTime(LocalDateTime.now());
            order.setCreateTime(LocalDateTime.now());

            orderRepository.save(order);

            // Mark coupon as used if applied
            if (payDTO.getCoupons() != null && !payDTO.getCoupons().isEmpty()) {
                Long couponId = payDTO.getCoupons().get(0).getId();
                if (couponId != null) {
                    myCouponRepository.useCoupon(couponId);
                }
            }

            log.info("Seckill order successfully created: {}", order.getOrderNo());
        } catch (Exception e) {
            log.error("Exception processing seckill order message: {}", e.getMessage(), e);
            compensateRedis(stockKey, userKey, buyNum);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private void compensateRedis(String stockKey, String userKey, int buyNum) {
        stringRedisTemplate.opsForValue().increment(stockKey, buyNum);
        stringRedisTemplate.delete(userKey);
    }
}
