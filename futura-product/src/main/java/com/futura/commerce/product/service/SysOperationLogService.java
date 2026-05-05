package com.futura.commerce.product.service;

import com.futura.commerce.mbg.model.SysOperationLog;

import java.util.List;

/**
 * Service interface for recording and querying system operation logs
 *
 * @author Vitalii
 */
public interface SysOperationLogService {

    List<SysOperationLog> findAll();

    List<SysOperationLog> findByBusinessId(Long businessId);

    SysOperationLog save(SysOperationLog log);
}
