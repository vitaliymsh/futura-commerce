package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain entity mapping table sms_promotion
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "sms_promotion")
public class SmsPromotion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long adminId;
    private Integer promotionType;
    private Long categoryId;
    private Long productId;
    private String promotionName;
    private BigDecimal price;
    private Integer days;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
    private BigDecimal payAmount;
    private LocalDateTime payTime;
    private Integer isCategory;
    private Long quota;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
