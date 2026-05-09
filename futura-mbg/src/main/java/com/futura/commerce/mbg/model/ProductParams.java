package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

/**
 * Domain entity mapping table product_params
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "product_params")
public class ProductParams implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "products_id")
    private Long productsId;

    private String brand;

    private String model;

    private String material;

    private String origin;
}
