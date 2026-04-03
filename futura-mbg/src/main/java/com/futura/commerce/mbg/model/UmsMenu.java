package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;

/**
 * Domain entity mapping table ums_menu
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "ums_menu")
public class UmsMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long parentId;
    private String title;
    private String path;
    private String icon;
    private Integer sort;
    private Integer type;
    private String permission;
    private Integer status;
}
