package com.futura.commerce.admin.service;

import com.futura.commerce.common.baseCommon.CommonResult;

/**
 * Service interface for administrative user operations
 *
 * @author Vitalii
 */
public interface UmsAdminService {

    CommonResult<String> login(String username, String password);
}
