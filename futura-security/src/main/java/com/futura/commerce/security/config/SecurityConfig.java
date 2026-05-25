package com.futura.commerce.security.config;

import com.futura.commerce.security.component.RestAuthenticationEntryPoint;
import com.futura.commerce.security.component.RestfulAccessDeniedHandler;
import com.futura.commerce.security.filter.JwtUserTokenFilter;
import com.futura.commerce.security.service.impl.AdminUserDetailsService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration center
 *
 * @author Vitalii
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Resource
    private AdminUserDetailsService userDetailsService;

    @Resource
    private JwtUserTokenFilter jwtUserTokenFilter;

    @Resource
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Resource
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. disable csrf
                .csrf(csrf -> csrf.disable())
                // 2. stateless session for jwt
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 3. route permission rules
                .authorizeHttpRequests(auth -> auth
                        // allow login and registration endpoints
                        .requestMatchers("/auth/**", "/user/auth/**", "/user/login", "/user/register", "/api/user/auth/**").permitAll()
                        // allow static image / avatar access
                        .requestMatchers("/pic/**").permitAll()
                        // allow swagger api documentation and webjars
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/webjars/**").permitAll()
                        // allow customer browsing and storefront read endpoints
                        .requestMatchers("/user/**", "/api/user/**").permitAll()
                        // allow internal microservice endpoints
                        .requestMatchers("/Pms_promotion/**", "/Oms_order/**", "/Oms_delivery/**", "/Oms_deliveryTrace/**", "/Oms_item/**",
                                "/Sms_seckill/**", "/Sms_skill/**", "/Sms_activity/**",
                                "/goodList", "/goodsPagination", "/search", "/selectSku",
                                "/goods/categories/**", "/goods/**", "/Sku/**", "/dashboard/**",
                                "/order/**", "/delivery/**", "/deliveryTrace/**", "/item/**",
                                "/after/**", "/review/**",
                                "/promotion/**", "/skill/**", "/activity/**", "/ai/**").permitAll()
                        // all other endpoints require authentication
                        .anyRequest().authenticated())
                // 4. register jwt filter
                .addFilterBefore(jwtUserTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 5. custom exception handlers
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                        .accessDeniedHandler(restfulAccessDeniedHandler))
                // 6. enable cors
                .cors(cors -> {});

        http.authenticationProvider(daoAuthenticationProvider());
        return http.build();
    }

    /**
     * Password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication provider backed by user details service
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Authentication manager bean
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfig) throws Exception {
        return authenticationConfig.getAuthenticationManager();
    }
}
