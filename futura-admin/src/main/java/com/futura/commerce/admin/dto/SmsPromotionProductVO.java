package com.futura.commerce.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Promotion product view object
 *
 * @author Vitalii
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsPromotionProductVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long productId;
    private String pic;
    private String name;
    private BigDecimal price;
    private Integer isPromotion;
    private Long quota;
    private Integer status;
    private String statusDesc;
    private Long validDays;
    private LocalDateTime payTime;
    private LocalDateTime createTime;
}
