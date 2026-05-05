package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Domain entity mapping table my_coupon
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "my_coupon")
public class MyCoupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Customer user ID
     */
    private Long userId;

    /**
     * Coupon definition ID
     */
    private Long couponId;

    /**
     * Coupon type
     */
    private Long couponType;

    /**
     * Coupon status: 0-unused, 1-used, 2-expired
     */
    private Integer status;

    /**
     * Time received
     */
    private LocalDateTime receiveTime;

    /**
     * Time used
     */
    private LocalDateTime useTime;
}
