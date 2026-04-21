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

    private String activityName;
    private Integer status;

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
    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
