package com.futura.commerce.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Merged order and delivery projection VO
 *
 * @author Vitalii
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OmsOrderAndDeliveryVO {
    private Long orderId;
    private String orderNo;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private Integer deliveryStatus;
    private LocalDateTime signTime;
    private LocalDateTime deliveryTime;
    private String deliveryUser;
}
