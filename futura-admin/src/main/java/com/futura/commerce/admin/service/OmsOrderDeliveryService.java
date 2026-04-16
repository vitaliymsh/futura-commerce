package com.futura.commerce.admin.service;

import com.futura.commerce.admin.vo.OmsDeliveryVO;
import com.futura.commerce.common.api.CommonResult;

import java.util.List;

/**
 * Service interface for order delivery management
 *
 * @author Vitalii
 */
public interface OmsOrderDeliveryService {

    CommonResult<List<OmsDeliveryVO>> selcetOrderAndDeliveryList();

    CommonResult<?> getLogisticsById(Long id);

    CommonResult<?> search(String orderNo, Integer deliveryStatus);
}
