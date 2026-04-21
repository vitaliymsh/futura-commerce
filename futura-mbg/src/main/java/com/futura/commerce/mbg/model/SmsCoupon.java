package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Coupon promotion entity mapping sms_coupon
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "sms_coupon")
public class SmsCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long activityId;
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
}
