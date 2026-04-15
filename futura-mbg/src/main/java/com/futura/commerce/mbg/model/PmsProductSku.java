package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain entity mapping table pms_product_sku
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "pms_product_sku")
public class PmsProductSku implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private String skuCode;
    private String model;
    private String spec;
    private String size;
    private BigDecimal price;
    private BigDecimal cost;
    private BigDecimal weight;
    private Integer stock;
    private Integer deletedSku;
    private Integer skuStatus;
    private String pic;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
