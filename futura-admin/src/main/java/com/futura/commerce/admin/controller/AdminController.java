package com.futura.commerce.admin.controller;

import com.futura.commerce.admin.service.UmsAdminService;
import com.futura.commerce.common.baseCommon.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Administrative user and account controller
 *
 * @author Vitalii
 */
@Slf4j
@RestController
@RequestMapping("/admin")
@Tag(name = "AdminController", description = "Administrator profile and quota management")
public class AdminController {

    @Resource
    private UmsAdminService umsAdminService;

    @GetMapping("/sum")
    @Operation(summary = "Recharge promotion package quota for current admin")
    public CommonResult<Long> sum(@RequestParam("id") Long id) {
        return umsAdminService.sum(id);
    }

    @PostMapping("/pic")
    @Operation(summary = "Upload administrator avatar", description = "Uploads an avatar image and updates current admin profile")
    public CommonResult<String> picture(@RequestParam("file") MultipartFile file) {
        return umsAdminService.uploadPicture(file);
    }

    @GetMapping("/pick")
    @Operation(summary = "Get administrator avatar", description = "Retrieves avatar URI of current admin user")
    public CommonResult<String> pick() {
        return umsAdminService.getPicture();
    }
}
