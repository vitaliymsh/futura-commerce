package com.futura.commerce.admin.service;

import com.futura.commerce.admin.dto.OmsOrderDeliveryTraceSearchDTO;
import com.futura.commerce.admin.dto.OmsOrderDeliveryUpdateDTO;
import com.futura.commerce.admin.vo.OmsDeliveryAndTraceVO;
import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.OmsOrderDeliveryTrace;

import java.util.List;
import java.util.Map;

/**
 * Service interface for delivery trace tracking
 *
 * @author Vitalii
 */
public interface OmsOrderDeliveryTraceService {

    List<OmsOrderDeliveryTrace> getTracesByDeliveryId(Long deliveryId);

    List<OmsDeliveryAndTraceVO> getDeliveryList();

    CommonResult<Map<String, Object>> getLogisticsById(Long id);

    CommonResult<?> updateDeliveryTraceInfo(OmsOrderDeliveryUpdateDTO dto);

    CommonResult<List<OmsDeliveryAndTraceVO>> searchDto(OmsOrderDeliveryTraceSearchDTO dto);
}
