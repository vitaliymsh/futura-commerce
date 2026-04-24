package com.futura.commerce.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Order item statistical analysis VO
 *
 * @author Vitalii
 */
@Data
public class OmsOrderItemVO {
    private Long productId;
    private Long orderId;
    private String productName;
    private String pic;
    private BigDecimal price;
    private Integer quantity;
    private Integer buyCounts;
    private String categoryName;
    private Long categoryId;
    private Integer totalQuantity;
    private LocalDateTime createTime;
    private LocalDateTime latestBuyTime;
    private List<Integer> avgAge;

    // Age distribution demographics
    private Integer ageUnder18Count;
    private Integer age18to25Count;
    private Integer age26to35Count;
    private Integer age36to45Count;
    private Integer ageOver46Count;
}
