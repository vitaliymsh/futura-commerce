package com.futura.commerce.common.dto;

import com.futura.commerce.mbg.model.PmsProductSku;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Data transfer object containing order info and associated product SKU items for AI assistance
 *
 * @author Vitalii
 */
@Data
public class AiOrderProductDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Unique order number identifier
     */
    private String orderNo;

    /**
     * Aggregated order total amount
     */
    private BigDecimal totalAmount;

    /**
     * Total item count / quantity purchased
     */
    private Integer productQuantity;

    /**
     * List of product SKU entity details associated with order items
     */
    private List<PmsProductSku> productList;
}
