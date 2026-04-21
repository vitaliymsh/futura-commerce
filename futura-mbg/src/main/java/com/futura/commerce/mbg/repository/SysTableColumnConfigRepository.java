package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.SysTableColumnConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for SysTableColumnConfig
 *
 * @author Vitalii
 */
@Repository
public interface SysTableColumnConfigRepository extends JpaRepository<SysTableColumnConfig, Long>, JpaSpecificationExecutor<SysTableColumnConfig> {

    List<SysTableColumnConfig> findByPageCodeAndAdminId(String pageCode, Long adminId);

    List<SysTableColumnConfig> findByPageCodeAndAdminIdAndIsShow(String pageCode, Long adminId, Integer isShow);
}
