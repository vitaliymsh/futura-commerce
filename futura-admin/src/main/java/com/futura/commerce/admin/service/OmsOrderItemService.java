package com.futura.commerce.admin.service;

import com.futura.commerce.admin.vo.OmsOrderItemVO;
import com.futura.commerce.common.api.CommonResult;

import java.util.List;

/**
 * Service interface for order item analytics
 *
 * @author Vitalii
 */
public interface OmsOrderItemService {

    CommonResult<List<OmsOrderItemVO>> itemList();
}
