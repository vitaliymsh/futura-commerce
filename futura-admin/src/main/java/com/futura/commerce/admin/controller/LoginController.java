package com.futura.commerce.admin.controller;

import com.futura.commerce.admin.service.UmsAdminService;
import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.UmsAdmin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "LoginController", description = "Authentication & login management")
public class LoginController {

    @Resource
    private UmsAdminService umsAdminService;

    @PostMapping("/login")
    @Operation(summary = "Admin login to retrieve JWT token")
    public CommonResult<String> login(@RequestBody UmsAdmin umsAdmin) {
        return umsAdminService.login(umsAdmin.getUsername(), umsAdmin.getPassword());
    }
}
