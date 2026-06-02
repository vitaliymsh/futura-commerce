package com.futura.commerce.order.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Admin review management DTO
 *
 * @author Vitalii
 */
@Data
public class AdminCommentDTO {
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
    private List<String> commentImage;
}
