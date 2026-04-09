package com.futura.commerce.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Admin permission and role assignment save DTO
 *
 * @author Vitalii
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UmsAdminSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long roleId;
}
