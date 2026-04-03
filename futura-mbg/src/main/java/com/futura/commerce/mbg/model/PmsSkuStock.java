package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Domain entity mapping table pms_sku_stock
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "pms_sku_stock")
public class PmsSkuStock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private String skuCode;
    private BigDecimal price;
    private Integer stock;
    private String spData;
}
