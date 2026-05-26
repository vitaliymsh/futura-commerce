package com.futura.commerce.product.util;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Helper component executing Redis Bloom filter operations (BF.ADD, BF.EXISTS)
 *
 * @author Vitalii
 */
@Slf4j
@Component
public class RedisBloomHelper {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void add(String bloomFilterKey, String value) {
        try {
            stringRedisTemplate.execute((RedisCallback<Void>) connection -> {
                connection.execute("BF.ADD", bloomFilterKey.getBytes(), value.getBytes());
                return null;
            });
        } catch (Exception e) {
            log.error("BF.ADD failed for key: {}, value: {}", bloomFilterKey, value, e);
        }
    }

    public boolean exists(String bloomFilterKey, String value) {
        try {
            Object result = stringRedisTemplate.execute((RedisCallback<Object>) connection ->
                    connection.execute("BF.EXISTS", bloomFilterKey.getBytes(), value.getBytes())
            );

            if (result instanceof Long l) {
                return l == 1L;
            } else if (result instanceof Integer i) {
                return i == 1;
            } else if (result instanceof byte[] b) {
                return "1".equals(new String(b));
            } else if (result instanceof Number n) {
                return n.intValue() == 1;
            }
        } catch (Exception e) {
            log.error("BF.EXISTS failed for key: {}, value: {}", bloomFilterKey, value, e);
        }

        return false;
    }
}
