package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Follow discount promotion entity mapping sms_follow_discount
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "sms_follow_discount")
public class SmsFollowDiscount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long activityId;
    private BigDecimal discountAmount;
    private Integer useCondition;
    private Integer useScope;
    private Integer participantCount;
    private Integer usedCount;
    private BigDecimal totalDiscountAmount;
}
