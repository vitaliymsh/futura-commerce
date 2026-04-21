package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Flash sale promotion entity mapping sms_seckill
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "sms_seckill")
public class SmsSeckill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long activityId;
    private Long skuId;
    private BigDecimal seckillPrice;
    private Integer stock;
    private Integer soldStock;
    private Integer limitQuantity;
    private Integer stockStatus;
    private Integer userLevelLimit;
    private Integer orderTypeLimit;
}
