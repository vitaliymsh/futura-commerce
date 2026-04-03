package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;

/**
 * Domain entity mapping table pms_product_category
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "pms_product_category")
public class PmsProductCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long parentId;
    private String name;
    private Integer sort;
    private String icon;
}
