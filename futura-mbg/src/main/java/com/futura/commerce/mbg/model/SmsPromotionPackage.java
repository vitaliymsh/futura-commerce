package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Domain entity mapping table ums_promotion_package
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "ums_promotion_package")
public class SmsPromotionPackage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal amount;
    private Long totalPromotionAmount;
    private Long promotionQuota;
    private Integer weight;
    private String remark;
}
