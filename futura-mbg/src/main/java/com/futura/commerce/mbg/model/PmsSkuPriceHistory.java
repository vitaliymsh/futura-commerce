package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * SKU price change history entity
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "pms_sku_price_history")
public class PmsSkuPriceHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long skuId;
    private String skuCode;
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private BigDecimal oldCost;
    private BigDecimal newCost;
    private String operator;
    private LocalDateTime updateTime;
    private String remark;
    private Integer changeType;
}
