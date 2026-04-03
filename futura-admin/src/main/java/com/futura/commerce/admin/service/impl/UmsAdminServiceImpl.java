package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.service.UmsAdminService;
import com.futura.commerce.common.baseCommon.CommonResult;
import com.futura.commerce.mbg.repository.UmsAdminRepository;
import com.futura.commerce.security.util.JwtTokenUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Service implementation for administrative user login and management
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class UmsAdminServiceImpl implements UmsAdminService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private UmsAdminRepository umsAdminRepository;

    @Override
    public CommonResult<String> login(String username, String password) {
        try {
            log.info("Attempting admin login for username: {}", username);
            // 1. Authenticate via Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            // 2. Generate JWT bearer token
            String token = jwtTokenUtil.generateToken(authentication);
            log.info("Login successful for username: {}", username);
            return CommonResult.success(token, "Login successful");
        } catch (Exception e) {
            log.error("Login failed for username {}: {}", username, e.getMessage());
            return CommonResult.failed("Invalid username or password");
        }
    }
}
