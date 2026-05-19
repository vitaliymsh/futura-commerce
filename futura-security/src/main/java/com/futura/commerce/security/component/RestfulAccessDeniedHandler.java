package com.futura.commerce.security.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.futura.commerce.common.api.CommonResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom 403 access denied handler
 *
 * @author Vitalii
 */
@Component
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().println(objectMapper.writeValueAsString(
                CommonResult.forbidden("Access denied: insufficient permissions")
        ));
        response.getWriter().flush();
    }
}
