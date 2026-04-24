package com.futura.commerce.seckill.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Unified marketing promotion activity list VO
 *
 * @author Vitalii
 */
@Data
public class ActivityListVO {
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

    // General activity attributes
    private String activityName;
    private Integer status;
    private Integer smsStatus;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getActivityMainId() { return activityMainId; }
    public void setActivityMainId(Long activityMainId) { this.activityMainId = activityMainId; }

    public Long getSkuId() { return skuId; }
    public void setSkuId(Long skuId) { this.skuId = skuId; }

    public BigDecimal getSeckillPrice() { return seckillPrice; }
    public void setSeckillPrice(BigDecimal seckillPrice) { this.seckillPrice = seckillPrice; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Integer getSoldStock() { return soldStock; }
    public void setSoldStock(Integer soldStock) { this.soldStock = soldStock; }

    public Integer getLimitQuantity() { return limitQuantity; }
    public void setLimitQuantity(Integer limitQuantity) { this.limitQuantity = limitQuantity; }

    public Integer getStockStatus() { return stockStatus; }
    public void setStockStatus(Integer stockStatus) { this.stockStatus = stockStatus; }

    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getPic() { return pic; }
    public void setPic(String pic) { this.pic = pic; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public Integer getCouponType() { return couponType; }
    public void setCouponType(Integer couponType) { this.couponType = couponType; }

    public BigDecimal getDiscountValue() { return discountValue; }
    public void setDiscountValue(BigDecimal discountValue) { this.discountValue = discountValue; }

    public BigDecimal getMinConsume() { return minConsume; }
    public void setMinConsume(BigDecimal minConsume) { this.minConsume = minConsume; }

    public Integer getUseScope() { return useScope; }
    public void setUseScope(Integer useScope) { this.useScope = useScope; }

    public Integer getTotalCount() { return totalCount; }
    public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }

    public Integer getReceivedCount() { return receivedCount; }
    public void setReceivedCount(Integer receivedCount) { this.receivedCount = receivedCount; }

    public Integer getUsedCount() { return usedCount; }
    public void setUsedCount(Integer usedCount) { this.usedCount = usedCount; }

    public Integer getLimitPerUser() { return limitPerUser; }
    public void setLimitPerUser(Integer limitPerUser) { this.limitPerUser = limitPerUser; }

    public LocalDateTime getReceiveStartTime() { return receiveStartTime; }
    public void setReceiveStartTime(LocalDateTime receiveStartTime) { this.receiveStartTime = receiveStartTime; }

    public LocalDateTime getReceiveEndTime() { return receiveEndTime; }
    public void setReceiveEndTime(LocalDateTime receiveEndTime) { this.receiveEndTime = receiveEndTime; }

    public LocalDateTime getUseStartTime() { return useStartTime; }
    public void setUseStartTime(LocalDateTime useStartTime) { this.useStartTime = useStartTime; }

    public LocalDateTime getUseEndTime() { return useEndTime; }
    public void setUseEndTime(LocalDateTime useEndTime) { this.useEndTime = useEndTime; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }

    public Integer getUseCondition() { return useCondition; }
    public void setUseCondition(Integer useCondition) { this.useCondition = useCondition; }

    public Integer getParticipantCount() { return participantCount; }
    public void setParticipantCount(Integer participantCount) { this.participantCount = participantCount; }

    public Integer getUsedCount2() { return usedCount2; }
    public void setUsedCount2(Integer usedCount2) { this.usedCount2 = usedCount2; }

    public BigDecimal getTotalDiscountAmount() { return totalDiscountAmount; }
    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) { this.totalDiscountAmount = totalDiscountAmount; }

    public BigDecimal getFullAmount() { return fullAmount; }
    public void setFullAmount(BigDecimal fullAmount) { this.fullAmount = fullAmount; }

    public BigDecimal getReductionAmount() { return reductionAmount; }
    public void setReductionAmount(BigDecimal reductionAmount) { this.reductionAmount = reductionAmount; }

    public BigDecimal getDiscountRate() { return discountRate; }
    public void setDiscountRate(BigDecimal discountRate) { this.discountRate = discountRate; }

    public Integer getRuleType() { return ruleType; }
    public void setRuleType(Integer ruleType) { this.ruleType = ruleType; }

    public Integer getOverlayRule() { return overlayRule; }
    public void setOverlayRule(Integer overlayRule) { this.overlayRule = overlayRule; }

    public Integer getOrderCount() { return orderCount; }
    public void setOrderCount(Integer orderCount) { this.orderCount = orderCount; }

    public BigDecimal getTotalDiscountAmount2() { return totalDiscountAmount2; }
    public void setTotalDiscountAmount2(BigDecimal totalDiscountAmount2) { this.totalDiscountAmount2 = totalDiscountAmount2; }

    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Integer getSmsStatus() { return smsStatus; }
    public void setSmsStatus(Integer smsStatus) { this.smsStatus = smsStatus; }
}
