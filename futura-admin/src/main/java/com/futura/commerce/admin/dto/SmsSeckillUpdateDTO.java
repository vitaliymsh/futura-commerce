package com.futura.commerce.admin.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Flash sale activity update DTO
 *
 * @author Vitalii
 */
@Data
public class SmsSeckillUpdateDTO {
    // Basic activity info
    private Long activityId;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;

    // Promotion rule
    private Long id;
    private Long skuId;
    private BigDecimal seckillPrice;
    private BigDecimal originalPrice;
    private Integer stock;
    private Integer limitQuantity;

    // Advanced settings
    private String userLevelLimit;
    private String orderTypeLimit;
    private Integer enableReminder;
    private String remark;
}
