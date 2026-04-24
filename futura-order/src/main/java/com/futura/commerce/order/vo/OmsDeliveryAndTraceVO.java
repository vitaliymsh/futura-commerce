package com.futura.commerce.order.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Order delivery and tracking details VO
 *
 * @author Vitalii
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OmsDeliveryAndTraceVO {
    private Long orderId;
    private String orderNo;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private Integer deliveryStatus;
    private LocalDateTime signTime;
    private LocalDateTime deliveryTime;
    private String deliveryUser;
    private Integer deliveryCompanyId;
    private String deliveryCompany;
    private String deliveryNo;
    private String operator;
    private LocalDateTime updateTime;
    private Integer traceStatus;
    private Integer sort;
    private LocalDateTime traceTime;
    private Integer delFlag;
    private List<Map<String, Object>> resultList;
}
