package com.futura.commerce.product.util;

import com.futura.commerce.mbg.model.UmsUser;
import com.futura.commerce.security.dto.UserLogin;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility to extract user id from security context
 *
 * @author Vitalii
 */
public class UserByTokenUtil {

    /**
     * Retrieve authenticated customer user ID
     *
     * @return User ID as string, or null if not authenticated
     */
    public static String getUserIdByToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if ("anonymousUser".equals(principal)) {
            return null;
        }

        if (principal instanceof UserLogin userLogin) {
            UmsUser user = userLogin.getUmsUser();
            return user != null && user.getId() != null ? user.getId().toString() : null;
        }

        return null;
    }
}
