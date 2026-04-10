package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain entity mapping table pms_product
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "pms_product")
public class PmsProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long categoryId;
    private BigDecimal price;
    private Integer stock;
    private String pic;
    private Integer sort;
    private Integer publishStatus;
    private Integer promoteWeight;
    private Integer isPromotion;
    private String description;
    private LocalDateTime createTime;
}
