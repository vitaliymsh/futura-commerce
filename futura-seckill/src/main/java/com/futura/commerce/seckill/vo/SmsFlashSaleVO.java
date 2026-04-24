package com.futura.commerce.seckill.vo;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }
    public String getPic() { return pic; }
    public void setPic(String pic) { this.pic = pic; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
}
