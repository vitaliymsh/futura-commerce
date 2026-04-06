package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.service.SmsPromotionPackageService;
import com.futura.commerce.admin.service.SmsPromotionRechargeService;
import com.futura.commerce.admin.service.UmsAdminService;
import com.futura.commerce.common.baseCommon.CommonResult;
import com.futura.commerce.mbg.model.SmsPromotionPackage;
import com.futura.commerce.mbg.model.SmsPromotionRecharge;
import com.futura.commerce.mbg.model.UmsAdmin;
import com.futura.commerce.mbg.repository.UmsAdminRepository;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for administrative user management and recharge
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof LoginUser loginUser)) {
            return CommonResult.unauthorized("Authentication required");
        }
        Long adminId = loginUser.getAdmin().getId();

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
}
