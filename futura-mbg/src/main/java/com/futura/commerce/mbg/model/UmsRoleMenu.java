package com.futura.commerce.mbg.model;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;

/**
 * Domain entity mapping table ums_role_menu
 *
 * @author Vitalii
 */
@Data
@Entity
@Table(name = "ums_role_menu")
public class UmsRoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long roleId;
    private Long menuId;
}
