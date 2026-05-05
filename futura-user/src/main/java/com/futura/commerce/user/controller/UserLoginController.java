package com.futura.commerce.user.controller;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.UmsUser;
import com.futura.commerce.user.dto.LoginDTO;
import com.futura.commerce.user.service.UmsUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Customer user authentication controller
 *
 * @author Vitalii
 */
@RestController
@RequestMapping("/user/auth")
@Tag(name = "UserLoginController", description = "Customer user authentication")
public class UserLoginController {

    @Resource
    private UmsUserService userService;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Login or auto-register customer by phone and password")
    public CommonResult<UmsUser> userLogin(@RequestBody LoginDTO userLogin) {
        return userService.userLogin(userLogin);
    }
}
