package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Domain entity mapping table oms_order_comment
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "oms_order_comment")
public class OmsOrderComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
     * Rating: 1-5 stars
     */
    private Integer score;

    /**
     * Comment type
     */
    private Integer type;

    /**
     * Comment time
     */
    private LocalDateTime commentTime;

    /**
     * Comment content text
     */
    private String commentContent;

    /**
     * Merchant reply text
     */
    private String replyContent;

    /**
     * Merchant reply time
     */
    private LocalDateTime replyTime;

    private LocalDateTime createTime;
}
