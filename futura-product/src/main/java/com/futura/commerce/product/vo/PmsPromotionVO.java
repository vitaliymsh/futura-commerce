package com.futura.commerce.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Product promotion view object
 *
 * @author Vitalii
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PmsPromotionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private Long categoryId;
    private String categoryName;
    private BigDecimal price;
    private Integer stock;
    private String pic;
    private Integer publishStatus;
}
