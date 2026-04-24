package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Domain entity mapping table oms_order_after_sales
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "oms_order_after_sales")
public class OmsOrderAfterSales implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * After sales number
     */
    private String afterSalesNo;

    /**
     * Order ID
     */
    private Long orderId;

    /**
     * Order item ID
     */
    private Long orderItemId;

    /**
     * User ID
     */
    private Long userId;

    /**
     * Product ID
     */
    private Long productId;

    /**
     * SKU ID
     */
    private Long skuId;

    /**
     * After-sales type: 1-refund only, 2-return and refund, 3-exchange
     */
    private Integer afterSalesType;

    /**
     * Application quantity
     */
    private Integer applyQuantity;

    /**
     * Application reason
     */
    private String applyReason;

    /**
     * Application status: 0-pending, 1-approved, 2-rejected, 3-completed
     */
    private Integer applyStatus;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
