package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.service.OmsOrderDeliveryTraceService;
import com.futura.commerce.admin.vo.OmsDeliveryAndTraceVO;
import com.futura.commerce.mbg.model.OmsOrder;
import com.futura.commerce.mbg.model.OmsOrderDelivery;
import com.futura.commerce.mbg.model.OmsOrderDeliveryTrace;
import com.futura.commerce.mbg.repository.OmsOrderDeliveryRepository;
import com.futura.commerce.mbg.repository.OmsOrderDeliveryTraceRepository;
import com.futura.commerce.mbg.repository.OmsOrderRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service implementation for delivery trace tracking
 *
 * @author Vitalii
 */
@Service
public class OmsOrderDeliveryTraceServiceImpl implements OmsOrderDeliveryTraceService {

    @Resource
    private OmsOrderDeliveryTraceRepository deliveryTraceRepository;

    @Resource
    private OmsOrderDeliveryRepository orderDeliveryRepository;

    @Resource
    private OmsOrderRepository orderRepository;

    @Override
    public List<OmsOrderDeliveryTrace> getTracesByDeliveryId(Long deliveryId) {
        if (deliveryId == null) {
            return List.of();
        }
        return deliveryTraceRepository.findByDeliveryId(deliveryId);
    }

    @Override
    public List<OmsDeliveryAndTraceVO> getDeliveryList() {
        List<OmsOrderDelivery> deliveries = orderDeliveryRepository.findAll();
        if (deliveries.isEmpty()) {
            return List.of();
        }

        List<Long> orderIds = deliveries.stream().map(OmsOrderDelivery::getOrderId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, OmsOrder> orderMap = orderRepository.findAllById(orderIds).stream()
                .collect(Collectors.toMap(OmsOrder::getId, o -> o, (k1, k2) -> k1));

        return deliveries.stream().map(d -> {
            OmsOrder order = orderMap.get(d.getOrderId());
            return new OmsDeliveryAndTraceVO(
                    d.getOrderId(),
                    d.getOrderNo(),
                    order != null ? order.getReceiverName() : null,
                    order != null ? order.getReceiverPhone() : null,
                    order != null ? order.getReceiverAddress() : null,
                    d.getDeliveryStatus(),
                    d.getSignTime(),
                    d.getDeliveryTime(),
                    d.getDeliveryUser(),
                    d.getDeliveryCompany(),
                    d.getDeliveryNo(),
                    null,
                    d.getUpdateTime()
            );
        }).collect(Collectors.toList());
    }
}
