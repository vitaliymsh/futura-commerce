package com.futura.commerce.order.service;

import com.futura.commerce.common.api.CommonResult;

import java.util.List;
import java.util.Map;

/**
 * Service interface for table column display configurations
 *
 * @author Vitalii
 */
public interface SysTableColumnConfigService {

    CommonResult<?> updateColumnConfig(List<Map<String, Object>> list, String pageCode);

    CommonResult<?> showColumn(String pageCode);
}
