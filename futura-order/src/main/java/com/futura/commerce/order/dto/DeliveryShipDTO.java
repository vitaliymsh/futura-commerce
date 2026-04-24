package com.futura.commerce.order.dto;

import lombok.Data;

/**
 * Order delivery shipment dispatch DTO
 *
 * @author Vitalii
 */
@Data
public class DeliveryShipDTO {
    private Integer deliveryCompanyId;
    private String trackingNo;
    private String deliveryUser;
    private String operator;
    private String phone;
    private String remark;
}
