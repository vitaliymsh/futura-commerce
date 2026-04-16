package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.service.OmsOrderDeliveryTraceService;
import com.futura.commerce.mbg.model.OmsOrderDeliveryTrace;
import com.futura.commerce.mbg.repository.OmsOrderDeliveryTraceRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for delivery trace tracking
 *
 * @author Vitalii
 */
@Service
public class OmsOrderDeliveryTraceServiceImpl implements OmsOrderDeliveryTraceService {

    @Resource
    private OmsOrderDeliveryTraceRepository deliveryTraceRepository;

    @Override
    public List<OmsOrderDeliveryTrace> getTracesByDeliveryId(Long deliveryId) {
        if (deliveryId == null) {
            return List.of();
        }
        return deliveryTraceRepository.findByDeliveryId(deliveryId);
    }
}
