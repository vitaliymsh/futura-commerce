package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain entity mapping table oms_order_finance
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "oms_order_finance")
public class OmsOrderFinance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Order ID
     */
    private Long orderId;

    /**
     * Order number
     */
    private String orderNo;

    /**
     * User ID
     */
    private Long userId;

    /**
     * Total amount
     */
    private BigDecimal totalAmount;

    /**
     * Actual paid amount
     */
    private BigDecimal payAmount;

    /**
     * Refunded amount
     */
    private BigDecimal refundAmount;

    /**
     * Payment method: 0-unpaid, 1-wechat, 2-alipay
     */
    private Integer payType;

    /**
     * Payment status: 0-pending, 1-paid, 2-refunding, 3-refunded
     */
    private Integer payStatus;

    /**
     * Payment time
     */
    private LocalDateTime payTime;

    /**
     * Refund time
     */
    private LocalDateTime refundTime;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
