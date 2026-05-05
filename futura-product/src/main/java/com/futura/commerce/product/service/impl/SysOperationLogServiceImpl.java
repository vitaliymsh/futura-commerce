package com.futura.commerce.product.service.impl;

import com.futura.commerce.mbg.model.SysOperationLog;
import com.futura.commerce.mbg.repository.SysOperationLogRepository;
import com.futura.commerce.product.service.SysOperationLogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for recording and querying system operation logs
 *
 * @author Vitalii
 */
@Service
public class SysOperationLogServiceImpl implements SysOperationLogService {

    @Resource
    private SysOperationLogRepository logRepository;

    @Override
    public List<SysOperationLog> findAll() {
        return logRepository.findAll();
    }

    @Override
    public List<SysOperationLog> findByBusinessId(Long businessId) {
        return logRepository.findAll().stream()
                .filter(l -> businessId != null && businessId.equals(l.getBusinessId()))
                .toList();
    }

    @Override
    public SysOperationLog save(SysOperationLog log) {
        return logRepository.save(log);
    }
}
