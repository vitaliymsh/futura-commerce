package com.futura.commerce.admin.dto;

import lombok.Data;

/**
 * Order delivery trace search DTO
 *
 * @author Vitalii
 */
@Data
public class OmsOrderDeliveryTraceSearchDTO {
    private String orderNo;
    private String deliveryNo;
    private Integer deliveryCompany;
    private String deliveryStartTime;
    private String deliveryEndTime;
    private Integer traceStatus;
    private String receiverName;
    private String receiverPhone;
    private String traceStartTime;
    private String traceEndTime;
}
