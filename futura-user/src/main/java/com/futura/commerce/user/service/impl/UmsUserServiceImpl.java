package com.futura.commerce.user.service.impl;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.UmsUser;
import com.futura.commerce.mbg.repository.UmsUserRepository;
import com.futura.commerce.user.dto.LoginDTO;
import com.futura.commerce.user.service.UmsUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service implementation for customer user accounts using Spring Data JPA
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class UmsUserServiceImpl implements UmsUserService {

    @Resource
    private UmsUserRepository userRepository;

    @Override
    public CommonResult<UmsUser> userLogin(LoginDTO userLogin) {
        log.info("Processing user login for phone: {}", userLogin.getPhone());
        if (userLogin.getPhone() == null || userLogin.getPassword() == null) {
            return CommonResult.validateFailed("Phone and password cannot be empty");
        }

        Optional<UmsUser> userOpt = userRepository.findByPhone(userLogin.getPhone());
        UmsUser user;
        if (userOpt.isEmpty()) {
            user = new UmsUser();
            user.setPhone(userLogin.getPhone());
            user.setUsername(userLogin.getPhone());
            user.setNickname("user_" + userLogin.getPhone().substring(Math.max(0, userLogin.getPhone().length() - 4)));
            user.setPassword(userLogin.getPassword());
            user.setCreateTime(LocalDateTime.now());
            user = userRepository.save(user);
        } else {
            user = userOpt.get();
            if (!user.getPassword().equals(userLogin.getPassword())) {
                return CommonResult.failed("Invalid password");
            }
        }
        log.info("User login successful for user id: {}", user.getId());
        return CommonResult.success(user, "Login successful");
    }
}
