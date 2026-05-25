package com.futura.commerce.user.controller;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.user.dto.LoginDTO;
import com.futura.commerce.user.service.UmsUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Customer user authentication controller
 *
 * @author Vitalii
 */
@RestController
@RequestMapping({"/user/auth", "/user"})
@Tag(name = "UserLoginController", description = "Customer user authentication")
public class UserLoginController {

    @Resource
    private UmsUserService userService;

    @PostMapping({"/login", "/auth/login"})
    @Operation(summary = "User login", description = "Login or auto-register customer by phone and password")
    public CommonResult<Map<String, Object>> userLogin(@RequestBody LoginDTO userLogin) {
        return userService.userLogin(userLogin);
    }

    @PostMapping({"/register", "/auth/register"})
    @Operation(summary = "User registration", description = "Register a new customer account")
    public CommonResult<String> register(@RequestBody LoginDTO userLogin) {
        return userService.register(userLogin);
    }
}
