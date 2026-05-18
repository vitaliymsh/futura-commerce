package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Customer cart item JPA entity
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "ums_cart")
public class UmsCart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "sku_id")
    private Long skuId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "selected")
    private Integer selected;

    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
}
