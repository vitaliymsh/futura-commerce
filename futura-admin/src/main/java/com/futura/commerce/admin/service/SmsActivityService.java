package com.futura.commerce.admin.service;

import com.futura.commerce.admin.vo.ActivityListVO;
import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.SmsActivity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing SmsActivity
 *
 * @author Vitalii
 */
public interface SmsActivityService {

    List<SmsActivity> findAll();

    Optional<SmsActivity> findById(Long id);

    SmsActivity save(SmsActivity entity);

    void deleteById(Long id);

    CommonResult<Page<ActivityListVO>> getActivityList(Integer pageNum, Integer pageSize, Integer type);

    CommonResult<Integer> activityStatus(Long id, Integer status);

    CommonResult<Page<ActivitySearchDTO>> activitySearch(Integer pageNum, Integer pageSize, ActivitySearchDTO activitySearchDTO);
}
