package com.futura.commerce.admin.service.impl;

import com.futura.commerce.common.service.OperationLogService;
import com.futura.commerce.mbg.model.SysOperationLog;
import com.futura.commerce.mbg.repository.SysOperationLogRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * Operation log service implementation in admin
 *
 * @author Vitalii
 */
@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Resource
    private SysOperationLogRepository sysOperationLogRepository;

    @Override
    public void save(SysOperationLog sysOperationLog) {
        if (sysOperationLog != null) {
            sysOperationLogRepository.save(sysOperationLog);
        }
    }
}
