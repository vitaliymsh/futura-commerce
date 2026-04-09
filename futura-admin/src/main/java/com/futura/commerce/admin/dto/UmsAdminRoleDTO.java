package com.futura.commerce.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Admin role info DTO
 *
 * @author Vitalii
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UmsAdminRoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long roleId;
    private String name;
}
