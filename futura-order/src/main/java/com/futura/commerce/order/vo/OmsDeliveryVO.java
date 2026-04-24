package com.futura.commerce.order.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Order delivery summary VO
 *
 * @author Vitalii
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OmsDeliveryVO {
    private Long orderId;
    private String orderNo;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private Integer deliveryStatus;
    private LocalDateTime signTime;
    private LocalDateTime deliveryTime;
    private String deliveryUser;
    private String deliveryCompany;
    private String deliveryNo;
}
