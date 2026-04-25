package com.futura.commerce.order.util;

import com.futura.commerce.mbg.model.UmsAdmin;
import com.futura.commerce.security.dto.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility for extracting current user and operator id from authentication token context
 *
 * @author Vitalii
 */
public class UserByTokenUtil {

    /**
     * Retrieve the current login admin user ID as string
     *
     * @return admin ID or null if unauthenticated
     */
    public static String getUserIdByToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof LoginUser) {
                LoginUser loginUser = (LoginUser) principal;
                UmsAdmin admin = loginUser.getAdmin();
                if (admin != null && admin.getId() != null) {
                    return admin.getId().toString();
                }
            }
        }
        return null;
    }
}
