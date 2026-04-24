package com.futura.commerce.order.service.impl;

import com.futura.commerce.order.service.SysTableColumnConfigService;
import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.SysTableColumnConfig;
import com.futura.commerce.mbg.repository.SysTableColumnConfigRepository;
import com.futura.commerce.security.domain.LoginUser;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service implementation for table column display configurations
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class SysTableColumnConfigServiceImpl implements SysTableColumnConfigService {

    @Resource
    private SysTableColumnConfigRepository columnConfigRepository;

    @Override
    public CommonResult<?> updateColumnConfig(List<Map<String, Object>> list, String pageCode) {
        Long adminId = getCurrentAdminId();
        if (list == null || list.isEmpty()) {
            return CommonResult.failed("Invalid configuration data");
        }

        List<SysTableColumnConfig> configs = columnConfigRepository.findByPageCodeAndAdminId(pageCode, adminId);
        LocalDateTime now = LocalDateTime.now();

        for (Map<String, Object> map : list) {
            String columnCode = (String) map.get("columnCode");
            Integer sortNum = map.get("sortNum") instanceof Number num ? num.intValue() : 0;
            Integer isShow = map.get("isShow") instanceof Number num ? num.intValue() : 1;

            SysTableColumnConfig config = configs.stream()
                    .filter(c -> c.getColumnCode().equals(columnCode))
                    .findFirst()
                    .orElse(null);

            if (config == null) {
                config = new SysTableColumnConfig();
                config.setAdminId(adminId);
                config.setPageCode(pageCode);
                config.setColumnCode(columnCode);
                config.setColumnName((String) map.getOrDefault("columnName", columnCode));
                config.setCreateTime(now);
            }

            config.setIsShow(isShow);
            config.setSortNum(sortNum);
            config.setUpdateTime(now);
            columnConfigRepository.save(config);
        }

        return CommonResult.success("Column configuration updated successfully");
    }

    @Override
    public CommonResult<?> showColumn(String pageCode) {
        Long adminId = getCurrentAdminId();
        List<SysTableColumnConfig> list = columnConfigRepository.findByPageCodeAndAdminIdAndIsShow(pageCode, adminId, 1);
        if (list.isEmpty()) {
            return CommonResult.success(new ArrayList<>(), "No custom configuration found");
        }
        return CommonResult.success(list, "Fetched column configuration successfully");
    }

    private Long getCurrentAdminId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser loginUser) {
            if (loginUser.getAdmin() != null) {
                return loginUser.getAdmin().getId();
            }
        }
        return 1L;
    }
}
