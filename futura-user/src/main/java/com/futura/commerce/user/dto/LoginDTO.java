package com.futura.commerce.user.dto;

import lombok.Data;

/**
 * User login request data transfer object
 *
 * @author Vitalii
 */
@Data
public class LoginDTO {
    private String username;
    private String phone;
    private String password;

    public String getEffectivePhone() {
        if (phone != null && !phone.trim().isEmpty()) {
            return phone.trim();
        }
        if (username != null && !username.trim().isEmpty()) {
            return username.trim();
        }
        return null;
    }
}

