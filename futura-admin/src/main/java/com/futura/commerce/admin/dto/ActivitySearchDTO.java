package com.futura.commerce.admin.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Universal marketing activity search DTO
 *
 * @author Vitalii
 */
@Data
public class ActivitySearchDTO {
    private Long id;
    private Long activityMainId;
    private Long skuId;
    private BigDecimal seckillPrice;
    private Integer stock;
    private Integer soldStock;
    private Integer limitQuantity;
    private Integer stockStatus;
    private BigDecimal originalPrice;
    private String productName;
    private String pic;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // Coupon fields
    private Integer couponType;
    private BigDecimal discountValue;
    private BigDecimal minConsume;
    private Integer useScope;
    private Integer totalCount;
    private Integer receivedCount;
    private Integer usedCount;
    private Integer limitPerUser;
    private LocalDateTime receiveStartTime;
    private LocalDateTime receiveEndTime;
    private LocalDateTime useStartTime;
    private LocalDateTime useEndTime;

    // Follow discount fields
    private BigDecimal discountAmount;
    private Integer useCondition;
    private Integer participantCount;
    private Integer usedCount2;
    private BigDecimal totalDiscountAmount;

    // Full reduction fields
    private BigDecimal fullAmount;
    private BigDecimal reductionAmount;
    private BigDecimal discountRate;
    private Integer ruleType;
    private Integer overlayRule;
    private Integer orderCount;
    private BigDecimal totalDiscountAmount2;

    // General activity fields
    private String activityName;
    private Integer status;
}
