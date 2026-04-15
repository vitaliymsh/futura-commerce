package com.futura.commerce.admin.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * SKU search criteria DTO
 *
 * @author Vitalii
 */
@Data
public class PmsSkuSearchDTO {
    private String productName;
    private String skuCode;
    private String spec;
    private String skuStatus;
    private Double priceMin;
    private Double priceMax;
    private Double costMin;
    private Double costMax;
    private Integer stockMin;
    private Integer stockMax;
    private Double weightMin;
    private Double weightMax;
    private LocalDateTime createTimeStart;
    private LocalDateTime createTimeEnd;
    private LocalDateTime updateTimeStart;
    private LocalDateTime updateTimeEnd;
    private Integer page;
    private Integer pageSize;
}
