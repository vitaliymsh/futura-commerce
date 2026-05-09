package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Domain entity mapping table pms_product_feature
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "pms_product_feature")
public class PmsProductFeature implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "feature_title")
    private String featureTitle;

    @Column(name = "feature_desc")
    private String featureDesc;

    private String icon;

    private Integer sort;

    private String description;

    @Column(name = "create_time")
    private LocalDateTime createTime;
}
