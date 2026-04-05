package com.futura.commerce.security.filter;

import com.futura.commerce.security.service.impl.AdminUserDetailsService;
import com.futura.commerce.security.util.JwtTokenUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT authentication filter inspecting bearer token header
 *
 * @author Vitalii
 */
@Component
public class JwtUserTokenFilter extends OncePerRequestFilter {

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private AdminUserDetailsService adminUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. extract bearer token from header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. validate token bearer
        String token = authHeader.substring(7);
        if (!jwtTokenUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. fetch user details and authorities from principal
        UserDetails userDetails;
        String username = jwtTokenUtil.getUsernameFromToken(token);
        try {
            userDetails = adminUserDetailsService.loadUserByUsername(username);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }

        // 4. assemble authentication token and establish security context
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 5. auto-refresh token and attach to response header
        String newToken = jwtTokenUtil.refreshToken(token);
        if (newToken != null) {
            response.setHeader("Authorization", "Bearer " + newToken);
        }

        // 6. continue filter chain
        filterChain.doFilter(request, response);
    }
}
