package com.futura.commerce.product.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Spring application event for invalidating product cache
 * Replaces binlog/Canal polling architecture with clean decoupled event-driven cache invalidation.
 *
 * @author Vitalii
 */
@Getter
public class ProductCacheEvictEvent extends ApplicationEvent {

    private final Long productId;

    public ProductCacheEvictEvent(Object source, Long productId) {
        super(source);
        this.productId = productId;
    }
}
