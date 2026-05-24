package com.futura.commerce.common.interceptor;

import com.futura.commerce.common.util.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor to automatically extract and populate customer userId into ThreadLocal context
 *
 * @author Vitalii
 */
@Component
public class UserAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long userId = null;
        try {
            String userIdHeader = request.getHeader("userId");
            if (userIdHeader != null && !userIdHeader.isBlank()) {
                userId = Long.parseLong(userIdHeader.trim());
            }

            if (userId == null) {
                String userIdParam = request.getParameter("userId");
                if (userIdParam != null && !userIdParam.isBlank()) {
                    userId = Long.parseLong(userIdParam.trim());
                }
            }

            if (userId != null) {
                UserUtil.setUserId(userId);
            }
        } catch (NumberFormatException ignored) {
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserUtil.clear();
    }
}
