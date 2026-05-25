package com.futura.commerce.user.service;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.user.dto.LoginDTO;

import java.util.Map;

/**
 * Service interface for customer user account operations
 *
 * @author Vitalii
 */
public interface UmsUserService {

    CommonResult<Map<String, Object>> userLogin(LoginDTO userLogin);

    CommonResult<String> register(LoginDTO userLogin);
}
