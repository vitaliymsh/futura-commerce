package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

/**
 * Domain entity mapping table sms_activity_product
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "sms_activity_product")
public class SmsActivityProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Associated activity ID
     */
    private Long activityId;

    /**
     * Product ID
     */
    private Long productId;

    /**
     * Category ID
     */
    private Long categoryId;
}
