package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.dto.UmsAdminRoleDTO;
import com.futura.commerce.admin.dto.UmsAdminSaveDTO;
import com.futura.commerce.admin.service.SmsPromotionPackageService;
import com.futura.commerce.admin.service.SmsPromotionRechargeService;
import com.futura.commerce.admin.service.UmsAdminService;
import com.futura.commerce.common.baseCommon.CommonResult;
import com.futura.commerce.mbg.model.*;
import com.futura.commerce.mbg.repository.UmsAdminRepository;
import com.futura.commerce.mbg.repository.UmsMenuRepository;
import com.futura.commerce.mbg.repository.UmsRoleMenuRepository;
import com.futura.commerce.mbg.repository.UmsRoleRepository;
import com.futura.commerce.security.dto.LoginUser;
import com.futura.commerce.security.util.JwtTokenUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service implementation for administrative user management, recharge, and profile
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

    @Resource
    private SmsPromotionPackageService smsPromotionPackageService;

    @Resource
    private SmsPromotionRechargeService smsPromotionRechargeService;

    @Resource
    private UmsRoleRepository umsRoleRepository;

    @Resource
    private UmsRoleMenuRepository umsRoleMenuRepository;

    @Resource
    private UmsMenuRepository umsMenuRepository;

    @Override
    public List<UmsAdmin> findAll() {
        return umsAdminRepository.findAll();
    }

    @Override
    public Optional<UmsAdmin> findById(Long id) {
        return umsAdminRepository.findById(id);
    }

    @Override
    public UmsAdmin save(UmsAdmin entity) {
        return umsAdminRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        umsAdminRepository.deleteById(id);
    }

    @Override
    public CommonResult<String> login(String username, String password) {
        try {
            log.info("Attempting admin login for username: {}", username);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            String token = jwtTokenUtil.generateToken(authentication);
            log.info("Login successful for username: {}", username);
            return CommonResult.success(token, "Login successful");
        } catch (Exception e) {
            log.error("Login failed for username {}: {}", username, e.getMessage());
            return CommonResult.failed("Invalid username or password");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Long> sum(Long packageId) {
        // 1. Verify promotion package
        Optional<SmsPromotionPackage> packageOpt = smsPromotionPackageService.findById(packageId);
        if (packageOpt.isEmpty()) {
            return CommonResult.failed("Promotion package not found");
        }
        SmsPromotionPackage pkg = packageOpt.get();
        Long price = pkg.getTotalPromotionAmount() != null ? pkg.getTotalPromotionAmount() : 0L;
        Long quota = pkg.getPromotionQuota() != null ? pkg.getPromotionQuota() : 0L;

        // 2. Extract current admin from security context
        Long adminId = getCurrentAdminId();
        if (adminId == null) {
            return CommonResult.unauthorized("Authentication required");
        }

        Optional<UmsAdmin> adminOpt = umsAdminRepository.findById(adminId);
        if (adminOpt.isEmpty()) {
            return CommonResult.failed("User account not found");
        }
        UmsAdmin admin = adminOpt.get();

        long balance = admin.getPrice() != null ? admin.getPrice().longValue() : 0L;
        long currentQuota = admin.getPromotionQuota() != null ? admin.getPromotionQuota() : 0L;

        balance -= price;
        currentQuota += quota;

        if (balance < 0) {
            return CommonResult.failed("Insufficient account balance");
        }

        admin.setPrice(BigDecimal.valueOf(balance));
        admin.setPromotionQuota(currentQuota);
        umsAdminRepository.save(admin);

        // 3. Record recharge transaction
        SmsPromotionRecharge recharge = new SmsPromotionRecharge();
        recharge.setAdminId(adminId);
        recharge.setPackageId(packageId);
        recharge.setAmount(BigDecimal.valueOf(price));
        recharge.setQuota(quota);
        recharge.setStatus(1); // 1 = paid
        recharge.setRechargeTime(LocalDateTime.now());
        recharge.setCreateTime(LocalDateTime.now());
        recharge.setUpdateTime(LocalDateTime.now());
        smsPromotionRechargeService.save(recharge);

        return CommonResult.success(currentQuota, "Promotion package recharged successfully");
    }

    @Override
    public CommonResult<String> uploadPicture(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return CommonResult.failed("Upload failed: no file provided");
        }
        try {
            String filename = file.getOriginalFilename();
            String suffix = ".jpg";
            if (filename != null && filename.contains(".")) {
                suffix = filename.substring(filename.lastIndexOf("."));
            }
            String newFileName = UUID.randomUUID().toString() + suffix;

            // store under standard relative uploads directory
            String uploadPath = System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "pic" + File.separator;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            File destFile = new File(uploadDir, newFileName);
            file.transferTo(destFile);

            Long adminId = getCurrentAdminId();
            if (adminId == null) {
                return CommonResult.unauthorized("Authentication required");
            }

            Optional<UmsAdmin> adminOpt = umsAdminRepository.findById(adminId);
            if (adminOpt.isEmpty()) {
                return CommonResult.failed("User account not found");
            }

            UmsAdmin admin = adminOpt.get();
            String accessUrl = "/pic/" + newFileName;
            admin.setAvatar(accessUrl);
            umsAdminRepository.save(admin);

            return CommonResult.success(accessUrl, "Avatar uploaded successfully");
        } catch (Exception e) {
            log.error("Avatar upload failed", e);
            return CommonResult.failed("Upload failed: " + e.getMessage());
        }
    }

    @Override
    public CommonResult<String> getPicture() {
        Long adminId = getCurrentAdminId();
        if (adminId == null) {
            return CommonResult.unauthorized("Authentication required");
        }

        Optional<UmsAdmin> adminOpt = umsAdminRepository.findById(adminId);
        if (adminOpt.isPresent() && adminOpt.get().getAvatar() != null) {
            return CommonResult.success(adminOpt.get().getAvatar(), "Avatar retrieved successfully");
        }
        return CommonResult.success(null, "No avatar uploaded for this user");
    }

    @Override
    public CommonResult<List<UmsAdmin>> getUserList() {
        List<UmsAdmin> list = umsAdminRepository.findAll();
        for (UmsAdmin umsAdmin : list) {
            Long roleId = umsAdmin.getRoleId();
            if (roleId != null) {
                Optional<UmsRole> roleOpt = umsRoleRepository.findById(roleId);
                roleOpt.ifPresent(r -> umsAdmin.setRoleName(r.getName()));

                List<UmsRoleMenu> roleMenuList = umsRoleMenuRepository.findByRoleId(roleId);
                List<String> permissionList = new ArrayList<>();
                for (UmsRoleMenu rm : roleMenuList) {
                    if (rm.getMenuId() != null) {
                        Optional<UmsMenu> menuOpt = umsMenuRepository.findById(rm.getMenuId());
                        if (menuOpt.isPresent() && StringUtils.hasText(menuOpt.get().getPermission())) {
                            permissionList.add(menuOpt.get().getPermission());
                        }
                    }
                }
                umsAdmin.setPermissionList(permissionList);
            }
        }
        return CommonResult.success(list, "User list retrieved successfully");
    }

    @Override
    public List<UmsAdminRoleDTO> getRoleListWithPermission() {
        List<UmsRole> roleList = umsRoleRepository.findAll();
        List<UmsAdminRoleDTO> result = new ArrayList<>();
        for (UmsRole role : roleList) {
            UmsAdminRoleDTO dto = new UmsAdminRoleDTO();
            dto.setRoleId(role.getId());
            dto.setName(role.getName());
            result.add(dto);
        }
        return result;
    }

    @Override
    public CommonResult<UmsAdminSaveDTO> saveById(UmsAdminSaveDTO umsAdminSaveDto) {
        if (umsAdminSaveDto == null || umsAdminSaveDto.getId() == null) {
            return CommonResult.failed("Parameters cannot be empty");
        }
        Long adminId = umsAdminSaveDto.getId();
        Optional<UmsAdmin> adminOpt = umsAdminRepository.findById(adminId);
        if (adminOpt.isEmpty()) {
            return CommonResult.failed("User not found");
        }
        UmsAdmin admin = adminOpt.get();
        admin.setRoleId(umsAdminSaveDto.getRoleId());
        umsAdminRepository.save(admin);
        return CommonResult.success(umsAdminSaveDto, "Admin role updated successfully");
    }

    private Long getCurrentAdminId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser loginUser) {
            if (loginUser.getAdmin() != null) {
                return loginUser.getAdmin().getId();
            }
        }
        return null;
    }
}
