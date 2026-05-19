package com.futura.commerce.admin.service;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.PmsDataStat;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service interface for platform statistics and analytics
 *
 * @author Vitalii
 */
public interface PmsDataStatService {

    List<PmsDataStat> findAll();

    Optional<PmsDataStat> findById(Long id);

    PmsDataStat save(PmsDataStat entity);

    void deleteById(Long id);

    /**
     * Retrieve aggregated user and sales dashboard metrics for date range
     */
    CommonResult<Map<String, Object>> selectUserDashboard(String startDate, String endDate);
}
