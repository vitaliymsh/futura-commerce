package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.SysOperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for SysOperationLog
 *
 * @author Vitalii
 */
@Repository
public interface SysOperationLogRepository extends JpaRepository<SysOperationLog, Long> {

    List<SysOperationLog> findByBusinessId(String businessId);

    List<SysOperationLog> findByModuleAndBusinessId(String module, String businessId);
}
