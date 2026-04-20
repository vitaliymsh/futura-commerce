package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Order delivery entity mapping table oms_order_delivery
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "oms_order_delivery")
public class OmsOrderDelivery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private String orderNo;
    private Integer deliveryCompanyId;
    private String deliveryUser;
    private String deliveryUserPhone;
    private String deliveryNo;
    private Integer deliveryStatus;
    private LocalDateTime deliveryTime;
    private LocalDateTime signTime;
    private String operator;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
