package com.futura.commerce.product.service;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.UmsCart;

import java.util.List;

/**
 * Service interface for customer cart operations
 *
 * @author Vitalii
 */
public interface UmsCartService {

    CommonResult<List<UmsCart>> cartList();
}
