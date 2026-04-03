package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Domain entity mapping table oms_delivery
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "oms_delivery")
public class OmsDelivery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private String orderNo;
    private String deliveryNo;
    private String deliveryCompany;
    private Integer deliveryStatus;
    private LocalDateTime deliveryTime;
    private String deliveryUser;
    private String deliveryPhone;
}
