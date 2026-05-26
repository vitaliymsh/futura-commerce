package com.futura.commerce.product.config;

/**
 * Redis key templates and TTL definitions for product module
 *
 * @author Vitalii
 */
public enum RedisKey {
    USER_BEHAVIOR("user:behavior:%s", 3600 * 24 * 7),
    USER_CATEGORY("user:category:%s", 3600 * 24 * 7),
    USER_VIEW_BLOOM("bloom:user:view:%s", 3600 * 24 * 7);

    private final String key;
    private final long expire;

    RedisKey(String key, long expire) {
        this.key = key;
        this.expire = expire;
    }

    public String getKey(Object... args) {
        return String.format(this.key, args);
    }

    public long getExpire() {
        return expire;
    }
}
