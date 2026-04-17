package com.futura.commerce.admin.service;

import com.futura.commerce.admin.vo.OmsDeliveryAndTraceVO;
import com.futura.commerce.mbg.model.OmsOrderDeliveryTrace;

import java.util.List;

/**
 * Service interface for delivery trace tracking
 *
 * @author Vitalii
 */
public interface OmsOrderDeliveryTraceService {

    List<OmsOrderDeliveryTrace> getTracesByDeliveryId(Long deliveryId);

    List<OmsDeliveryAndTraceVO> getDeliveryList();
}
