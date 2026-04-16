package com.futura.commerce.common.service;

import com.futura.commerce.mbg.model.SysOperationLog;

/**
 * Service interface for saving operation logs
 *
 * @author Vitalii
 */
public interface OperationLogService {
    void save(SysOperationLog sysOperationLog);
}
