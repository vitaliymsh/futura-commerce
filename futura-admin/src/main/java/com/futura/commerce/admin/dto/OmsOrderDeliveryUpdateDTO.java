package com.futura.commerce.admin.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Order delivery update DTO
 *
 * @author Vitalii
 */
@Data
public class OmsOrderDeliveryUpdateDTO {
    private Long orderId;
    private Integer deliveryCompanyId;
    private String deliveryCompany;
    private String deliveryNo;
    private String deliveryUser;
    private String deliveryPhone;
    private Integer deliveryStatus;
    private Integer traceStatus;
    private String modifyReason;
    private String receiverAddress;
    private String receiverName;
    private String receiverPhone;
    private String traceAddress;
    private Integer operatorId;
    private String operationType;
    private LocalDateTime traceTime;
}
