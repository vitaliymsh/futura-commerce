package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Delivery company dictionary entity mapping oms_delivery_company
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "oms_delivery_company")
public class OmsDeliveryCompany implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Carrier code (SF, YTO, ZTO, etc.)
     */
    private String companyCode;

    /**
     * Carrier full name
     */
    private String deliveryCompany;

    /**
     * Sort order
     */
    private Integer sort;

    /**
     * Status: 0 disabled, 1 active
     */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
