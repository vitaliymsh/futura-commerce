package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Domain entity mapping table sms_activity
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "sms_activity")
public class SmsActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Activity title
     */
    private String title;

    /**
     * Activity type: 1-seckill, 2-coupon, 3-follow discount, 4-full reduction
     */
    private Integer type;

    /**
     * Activity start time
     */
    private LocalDateTime startTime;

    /**
     * Activity end time
     */
    private LocalDateTime endTime;

    /**
     * Activity status: 0-not started, 1-in progress, 2-ended, 3-offline
     */
    private Integer status;

    /**
     * Activity description
     */
    private String description;

    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;

    /**
     * Deletion flag: 0-not deleted, 1-deleted
     */
    private Integer isDeleted;

    /**
     * 3-offline 4-online
     */
    private String smsStatus;

    /**
     * User level limit: 0-regular, 1-VIP, 2-SVIP
     */
    private Integer userLevelLimit;

    /**
     * Order type limit: 0-regular, 1-presale, 2-group buy
     */
    private Integer orderTypeLimit;
}
