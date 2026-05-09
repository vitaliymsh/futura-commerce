package com.futura.commerce.product.dto;

import com.futura.commerce.mbg.model.PmsProduct;
import com.futura.commerce.mbg.model.PmsProductFeature;
import com.futura.commerce.mbg.model.PmsProductSku;
import com.futura.commerce.mbg.model.ProductParams;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Product detail response DTO containing specifications, SKUs, and features
 *
 * @author Vitalii
 */
@Data
public class ProductDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Core product entity
     */
    private PmsProduct product;

    /**
     * Product SKU variants list
     */
    private List<PmsProductSku> skuList;

    /**
     * Product specification parameters list
     */
    private List<ProductParams> paramsList;

    /**
     * Product features list
     */
    private List<PmsProductFeature> featureList;
}
