package com.futura.commerce.security.filter;

import com.futura.commerce.security.service.impl.AdminUserDetailsService;
import com.futura.commerce.security.service.impl.UserDetailService;
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
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * JWT authentication filter inspecting bearer token header and establishing security context
 *
 * @author Vitalii
 */
@Component
public class JwtUserTokenFilter extends OncePerRequestFilter {

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private AdminUserDetailsService adminUserDetailsService;

    @Resource
    private UserDetailService userDetailService;

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final List<String> WHITE_LIST = Arrays.asList(
            "/auth/**",
            "/user/auth/**",
            "/user/login",
            "/user/register",
            "/api/user/auth/**",
            "/actuator/health",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/pic/**",
            "/user/**",
            "/Pms_promotion/**",
            "/goods/**",
            "/dashboard/**",
            "/Sku/**",
            "/promotion/**",
            "/user/comment/**",
            "/user/upload/**",
            "/user/item/**",
            "/order/**",
            "/delivery/**",
            "/deliveryTrace/**",
            "/item/**",
            "/ai/**",
            "/skill/**",
            "/activity/**",
            "/coupon/**",
            "/api/user/cart/**",
            "/api/user/category/**",
            "/api/user/product/**",
            "/api/user/comment/**",
            "/api/user/upload/**",
            "/api/user/item/**",
            "/api/user/order/**",
            "/api/user/coupon/**",
            "/api/user/skill/**"
    );



    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        if (token.isEmpty() || !jwtTokenUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        UserDetails userDetails;
        String username = jwtTokenUtil.getUsernameFromToken(token);
        try {
            String uri = request.getRequestURI();
            if (uri.startsWith("/user") || uri.startsWith("/api/user")) {
                userDetails = userDetailService.loadUserByUsername(username);
            } else {
                userDetails = adminUserDetailsService.loadUserByUsername(username);
            }
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        String newToken = jwtTokenUtil.refreshToken(token);
        if (newToken != null) {
            response.setHeader("Authorization", "Bearer " + newToken);
        }

        filterChain.doFilter(request, response);
    }
}
