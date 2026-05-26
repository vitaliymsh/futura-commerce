package com.futura.commerce.order.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for order comments and reviews
 *
 * @author Vitalii
 */
@Data
public class OrderCommentDTO {
    private Long id;
    private Long orderId;
    private Integer type;
    private Long orderItemId;
    private Long userId;
    private Long productId;
    private Long skuId;
    private Integer score;
    private Integer logisticsScore;
    private Integer serviceScore;
    private LocalDateTime commentTime;
    private String commentContent;
    private Integer isNow;
    private List<Long> tagIds;
    private String commentImage;
}
