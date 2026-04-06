package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain entity mapping table sms_promotion_recharge
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "sms_promotion_recharge")
public class SmsPromotionRecharge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long adminId;
    private Long packageId;
    private BigDecimal amount;
    private Integer status;
    private LocalDateTime rechargeTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Long quota;
}
