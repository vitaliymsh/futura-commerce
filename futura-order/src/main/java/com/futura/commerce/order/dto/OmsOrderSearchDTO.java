package com.futura.commerce.order.dto;

import lombok.Data;

/**
 * Order search criteria DTO
 *
 * @author Vitalii
 */
@Data
public class OmsOrderSearchDTO {
    private Integer pageNum;
    private Integer pageSize;
    private String orderNo;
    private String buyerInfo;
    private Integer status;
    private String startTime;
    private String endTime;
    private Integer minAmount;
    private Integer maxAmount;
    private Integer payType;
    private Integer deliveryStatus;
}
