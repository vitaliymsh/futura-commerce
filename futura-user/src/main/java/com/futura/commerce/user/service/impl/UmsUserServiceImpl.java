package com.futura.commerce.user.service.impl;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.UmsUser;
import com.futura.commerce.mbg.repository.UmsUserRepository;
import com.futura.commerce.security.util.JwtTokenUtil;
import com.futura.commerce.user.dto.LoginDTO;
import com.futura.commerce.user.service.UmsUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service implementation for customer user accounts using Spring Data JPA and token generation
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class UmsUserServiceImpl implements UmsUserService {

    @Resource
    private UmsUserRepository userRepository;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public CommonResult<Map<String, Object>> userLogin(LoginDTO userLogin) {
        String identifier = userLogin != null ? userLogin.getEffectivePhone() : null;
        log.info("Processing user login for identifier: {}", identifier);
        if (identifier == null || userLogin.getPassword() == null) {
            return CommonResult.validateFailed("Username/phone and password cannot be empty");
        }

        Optional<UmsUser> userOpt = userRepository.findByPhone(identifier);
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByUsername(identifier);
        }

        UmsUser user;
        if (userOpt.isEmpty()) {
            user = new UmsUser();
            user.setPhone(identifier);
            user.setUsername(identifier);
            user.setNickname("user_" + identifier.substring(Math.max(0, identifier.length() - 4)));
            user.setPassword(passwordEncoder.encode(userLogin.getPassword()));
            user.setCreateTime(LocalDateTime.now());
            user = userRepository.save(user);
        } else {
            user = userOpt.get();
            boolean passwordMatches = (user.getPassword() != null && passwordEncoder.matches(userLogin.getPassword(), user.getPassword()))
                    || (user.getPassword() != null && user.getPassword().equals(userLogin.getPassword()));
            if (!passwordMatches) {
                return CommonResult.failed("Invalid username or password");
            }
        }

        // Generate JWT token for user
        String tokenSubject = user.getPhone() != null ? user.getPhone() : user.getUsername();
        String token = jwtTokenUtil.generateTokenFromUsername(tokenSubject);
        log.info("User login successful: userId={}, username={}", user.getId(), user.getUsername());

        user.setPassword(null);
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("token", token);
        data.put("id", user.getId());
        data.put("nickname", user.getNickname());

        return CommonResult.success(data, "Login successful");
    }

    @Override
    public CommonResult<String> register(LoginDTO userLogin) {
        String identifier = userLogin != null ? userLogin.getEffectivePhone() : null;
        log.info("Registering customer user: identifier={}", identifier);
        if (identifier == null || userLogin.getPassword() == null) {
            return CommonResult.validateFailed("Username/phone and password cannot be empty");
        }

        Optional<UmsUser> existing = userRepository.findByPhone(identifier);
        if (existing.isEmpty()) {
            existing = userRepository.findByUsername(identifier);
        }
        if (existing.isPresent()) {
            return CommonResult.failed("User already exists");
        }

        UmsUser user = new UmsUser();
        user.setPhone(identifier);
        user.setUsername(identifier);
        user.setNickname("user_" + identifier.substring(Math.max(0, identifier.length() - 4)));
        user.setPassword(passwordEncoder.encode(userLogin.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        userRepository.save(user);

        return CommonResult.success("Registration successful");
    }
}
