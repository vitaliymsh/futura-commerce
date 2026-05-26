package com.futura.commerce.product.dto;

import lombok.Data;

import java.util.List;

/**
 * Elasticsearch document representation for product and SKU catalog
 *
 * @author Vitalii
 */
@Data
public class ProductSkuEsDoc {
    private Long productId;
    private String name;
    private String highlightName;
    private String pic;
    private Long categoryId;
    private String categoryName;
    private Integer publishStatus;
    private Double price;
    private String searchKey;
    private List<SkuInfo> skuList;
    private String allSpecs;

    @Data
    public static class SkuInfo {
        private Long skuId;
        private String size;
        private String spec;
        private Integer stock;
        private Integer skuStatus;
    }
}
