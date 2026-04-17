package com.futura.commerce.admin.service;

import com.futura.commerce.admin.vo.OmsOrderVO;
import com.futura.commerce.common.api.CommonResult;

/**
 * Service interface for order operations
 *
 * @author Vitalii
 */
public interface OmsOrderService {

    CommonResult<OmsOrderVO> getOrderDetailById(Long id);
}
