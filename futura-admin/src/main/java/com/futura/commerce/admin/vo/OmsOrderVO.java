package com.futura.commerce.admin.vo;

import com.futura.commerce.mbg.model.OmsOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Order details presentation VO
 *
 * @author Vitalii
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OmsOrderVO extends OmsOrder {
    private String deliveryCompany;
    private String orderNo;
    private Integer deliveryStatus;
    private LocalDateTime signTime;
    private String deliveryNo;

    private List<OrderItemVO> items;
    private DeliveryVO delivery;

    @Data
    public static class OrderItemVO {
        private String productName;
        private String pic;
        private String spec;
        private String productPrice;
        private Integer quantity;
        private String realAmount;
    }

    @Data
    public static class DeliveryVO {
        private String deliveryCompany;
        private String deliveryNo;
        private Integer deliveryStatus;
        private LocalDateTime deliveryTime;
        private LocalDateTime signTime;
        private String receiverName;
        private String receiverPhone;
        private String receiverAddress;
    }
}
