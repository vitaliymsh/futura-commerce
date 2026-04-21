package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Full reduction activity entity mapping sms_full_reduction
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "sms_full_reduction")
public class SmsFullReduction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long activityId;
    private BigDecimal fullAmount;
    private BigDecimal reductionAmount;
    private BigDecimal discountRate;
    private Integer ruleType;
    private Integer overlayRule;
    private Integer useScope;
    private Integer orderCount;
    private BigDecimal totalDiscountAmount;
}
