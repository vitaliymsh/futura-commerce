package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Promotion quota allocation log
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "sms_promotion_quota_log")
public class SmsPromotionQuotaLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long promotionId;
    private Long productId;
    private Integer addQuota;
    private LocalDateTime createTime;
    private Integer status; // 0 = active/allocated, 1 = revoked/recovered
    private Long adminId;
}
