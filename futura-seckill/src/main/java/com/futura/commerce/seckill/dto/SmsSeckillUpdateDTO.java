package com.futura.commerce.seckill.dto;

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

    public Long getActivityId() { return activityId; }
    public void setActivityId(Long activityId) { this.activityId = activityId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getSkuId() { return skuId; }
    public void setSkuId(Long skuId) { this.skuId = skuId; }
    public BigDecimal getSeckillPrice() { return seckillPrice; }
    public void setSeckillPrice(BigDecimal seckillPrice) { this.seckillPrice = seckillPrice; }
    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Integer getLimitQuantity() { return limitQuantity; }
    public void setLimitQuantity(Integer limitQuantity) { this.limitQuantity = limitQuantity; }
    public String getUserLevelLimit() { return userLevelLimit; }
    public void setUserLevelLimit(String userLevelLimit) { this.userLevelLimit = userLevelLimit; }
    public String getOrderTypeLimit() { return orderTypeLimit; }
    public void setOrderTypeLimit(String orderTypeLimit) { this.orderTypeLimit = orderTypeLimit; }
    public Integer getEnableReminder() { return enableReminder; }
    public void setEnableReminder(Integer enableReminder) { this.enableReminder = enableReminder; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
