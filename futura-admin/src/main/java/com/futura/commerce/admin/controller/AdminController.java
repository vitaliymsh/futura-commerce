package com.futura.commerce.admin.controller;

import com.futura.commerce.admin.dto.UmsAdminRoleDTO;
import com.futura.commerce.admin.dto.UmsAdminSaveDTO;
import com.futura.commerce.admin.service.UmsAdminService;
import com.futura.commerce.common.baseCommon.CommonResult;
import com.futura.commerce.mbg.model.UmsAdmin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','promotion:view')")
    @Operation(summary = "Recharge promotion package quota for current admin")
    public CommonResult<Long> sum(@RequestParam("id") Long id) {
        return umsAdminService.sum(id);
    }

    @PostMapping("/pic")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Upload administrator avatar", description = "Uploads an avatar image and updates current admin profile")
    public CommonResult<String> picture(@RequestParam("file") MultipartFile file) {
        return umsAdminService.uploadPicture(file);
    }

    @GetMapping("/pick")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get administrator avatar", description = "Retrieves avatar URI of current admin user")
    public CommonResult<String> pick() {
        return umsAdminService.getPicture();
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage')")
    @Operation(summary = "Get administrator user list", description = "Lists admin users with assigned roles and permission list")
    public CommonResult<List<UmsAdmin>> user() {
        return umsAdminService.getUserList();
    }

    @GetMapping("/role/listALL")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List all administrative roles")
    public CommonResult<List<UmsAdminRoleDTO>> roleListAll() {
        List<UmsAdminRoleDTO> list = umsAdminService.getRoleListWithPermission();
        return CommonResult.success(list, "Roles retrieved successfully");
    }

    @PostMapping("/save")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Save user role assignment")
    public CommonResult<UmsAdminSaveDTO> save(@RequestBody UmsAdminSaveDTO umsAdminSaveDto) {
        return umsAdminService.saveById(umsAdminSaveDto);
    }
}
