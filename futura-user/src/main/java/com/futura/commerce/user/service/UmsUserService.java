package com.futura.commerce.user.service;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.UmsUser;
import com.futura.commerce.user.dto.LoginDTO;

/**
 * Service interface for customer user account operations
 *
 * @author Vitalii
 */
public interface UmsUserService {

    CommonResult<UmsUser> userLogin(LoginDTO userLogin);
}
