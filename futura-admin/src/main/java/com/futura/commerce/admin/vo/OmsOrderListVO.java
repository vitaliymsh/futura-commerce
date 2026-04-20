package com.futura.commerce.admin.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Order summary list VO
 *
 * @author Vitalii
 */
@Data
public class OmsOrderListVO {
    private Long id;
    private Long userId;
    private String orderNo;
    private String orderSn;
    private String productName;
    private String productPic;
    private Integer productQuantity;
    private String buyerInfo;
    private LocalDateTime createTime;
    private LocalDateTime payTime;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private Integer status;
    private Integer deliveryStatus;
    private LocalDateTime deliveryTime;
}
