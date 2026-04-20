package com.futura.commerce.admin.dto;

import lombok.Data;

/**
 * Order delivery search DTO
 *
 * @author Vitalii
 */
@Data
public class OmsOrderDeliverySearchDTO {
    private Integer page;
    private Integer pageSize;
    private Integer offset;
    private String orderNo;
    private String deliveryNo;
    private String receiverPhone;
    private Integer deliveryStatus;

    public void setOffset() {
        if (page != null && pageSize != null) {
            this.offset = (page - 1) * pageSize;
        }
    }
}
