package com.futura.commerce.order.service;

import com.futura.commerce.order.dto.OmsOrderDeliveryTraceSearchDTO;
import com.futura.commerce.order.dto.OmsOrderDeliveryUpdateDTO;
import com.futura.commerce.order.export.OmsOrderDeliveryTraceExcel;
import com.futura.commerce.order.vo.OmsDeliveryAndTraceVO;
import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.OmsOrderDeliveryTrace;
import org.springframework.data.domain.Page;

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

    CommonResult<Page<OmsDeliveryAndTraceVO>> getDeliveryList(Integer pageNum, Integer pageSize);

    CommonResult<Map<String, Object>> getLogisticsById(Long id);

    CommonResult<?> updateDeliveryTraceInfo(OmsOrderDeliveryUpdateDTO dto);

    CommonResult<List<OmsDeliveryAndTraceVO>> searchDto(OmsOrderDeliveryTraceSearchDTO dto);

    CommonResult<?> setDeliveryTraceColumn(List<Map<String, Object>> list);

    CommonResult<List<OmsOrderDeliveryTraceExcel>> exportExcel(OmsOrderDeliveryTraceSearchDTO dto);
}
