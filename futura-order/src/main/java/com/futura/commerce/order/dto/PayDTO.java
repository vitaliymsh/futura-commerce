package com.futura.commerce.order.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Customer order payment request data transfer object
 *
 * @author Vitalii
 */
@Data
public class PayDTO {

    private AddressDTO address;
    private List<CouponItemDTO> coupons;
    private List<ProductItemDTO> products;
    private Long skuId;

    /**
     * Delivery shipping address
     */
    @Data
    public static class AddressDTO {
        private Long id;
        private String name;
        private String phone;
        private String province;
        private String city;
        private String district;
        private String detail;
    }

    /**
     * Applied coupon item
     */
    @Data
    public static class CouponItemDTO {
        private Long id;
        private Long couponId;
        private String name;
        private BigDecimal discount;
        private Integer status;
        private Long myCouponId;
    }

    /**
     * Order product item
     */
    @Data
    public static class ProductItemDTO {
        private Long productId;
        private Integer quantity;
        private String remark;
        private BigDecimal totalAmount;
        private String pic;
        private String orderNo;
        private BigDecimal payAmount;
        private Integer payType;
    }
}
