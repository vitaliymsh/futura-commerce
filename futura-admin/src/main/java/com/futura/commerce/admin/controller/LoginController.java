package com.futura.commerce.admin.controller;

import com.futura.commerce.admin.service.UmsAdminService;
import com.futura.commerce.common.baseCommon.CommonResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class LoginController {

    @Resource
    private UmsAdminService umsAdminService;

    @PostMapping("/login")
    public CommonResult<String> login(@RequestParam("username") String username,
                                      @RequestParam("password") String password) {
        return umsAdminService.login(username, password);
    }
}
