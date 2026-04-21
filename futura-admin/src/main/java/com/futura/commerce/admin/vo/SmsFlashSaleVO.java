package com.futura.commerce.admin.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Flash sale / seckill promotion item VO
 *
 * @author Vitalii
 */
@Data
public class SmsFlashSaleVO {
    private Long id;
    private Long skuId;
    private BigDecimal seckillPrice;
    private Integer stock;
    private Integer soldStock;
    private Integer limitQuantity;
    private Integer stockStatus;
    private BigDecimal originalPrice;
    private String productName;
    private String activityName;
    private String pic;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
