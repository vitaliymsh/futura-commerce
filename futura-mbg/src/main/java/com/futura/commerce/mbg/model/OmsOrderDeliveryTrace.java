package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Order delivery trace entity mapping oms_order_delivery_trace
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "oms_order_delivery_trace")
public class OmsOrderDeliveryTrace implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long deliveryId;
    private String traceStatus;
    private String traceLocation;
    private String traceContent;
    private LocalDateTime traceTime;
    private LocalDateTime createTime;
}
