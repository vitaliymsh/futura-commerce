package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Domain entity mapping table oms_order_comment
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "oms_order_comment")
public class OmsOrderComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private Long orderItemId;
    private Long userId;
    private Long productId;
    private Long skuId;
    private Integer score;
    private Integer logisticsScore;
    private Integer serviceScore;
    private Integer type;
    private LocalDateTime commentTime;
    private String commentContent;
    private Integer isNow;
    private String replyContent;
    private LocalDateTime replyTime;
    private LocalDateTime createTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getOrderItemId() { return orderItemId; }
    public void setOrderItemId(Long orderItemId) { this.orderItemId = orderItemId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Long getSkuId() { return skuId; }
    public void setSkuId(Long skuId) { this.skuId = skuId; }
    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
    public Integer getLogisticsScore() { return logisticsScore; }
    public void setLogisticsScore(Integer logisticsScore) { this.logisticsScore = logisticsScore; }
    public Integer getServiceScore() { return serviceScore; }
    public void setServiceScore(Integer serviceScore) { this.serviceScore = serviceScore; }
    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }
    public LocalDateTime getCommentTime() { return commentTime; }
    public void setCommentTime(LocalDateTime commentTime) { this.commentTime = commentTime; }
    public String getCommentContent() { return commentContent; }
    public void setCommentContent(String commentContent) { this.commentContent = commentContent; }
    public Integer getIsNow() { return isNow; }
    public void setIsNow(Integer isNow) { this.isNow = isNow; }
    public String getReplyContent() { return replyContent; }
    public void setReplyContent(String replyContent) { this.replyContent = replyContent; }
    public LocalDateTime getReplyTime() { return replyTime; }
    public void setReplyTime(LocalDateTime replyTime) { this.replyTime = replyTime; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
