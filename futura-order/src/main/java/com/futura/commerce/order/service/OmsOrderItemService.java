package com.futura.commerce.order.service;

import com.futura.commerce.common.dto.AiOrderProductDto;
import com.futura.commerce.order.vo.OmsOrderItemVO;
import com.futura.commerce.common.api.CommonResult;

import java.util.List;

/**
 * Service interface for order item analytics and lookups
 *
 * @author Vitalii
 */
public interface OmsOrderItemService {

    CommonResult<List<OmsOrderItemVO>> itemList();

    AiOrderProductDto getOrderAndProductByOrderNo(String orderNo);
}
