package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.service.OmsOrderDeliveryService;
import com.futura.commerce.admin.service.OmsOrderDeliveryTraceService;
import com.futura.commerce.admin.vo.OmsDeliveryVO;
import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.OmsOrder;
import com.futura.commerce.mbg.model.OmsOrderDelivery;
import com.futura.commerce.mbg.model.OmsOrderDeliveryTrace;
import com.futura.commerce.mbg.repository.OmsOrderDeliveryRepository;
import com.futura.commerce.mbg.repository.OmsOrderRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementation for order delivery management
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class OmsOrderDeliveryServiceImpl implements OmsOrderDeliveryService {

    @Resource
    private OmsOrderDeliveryRepository orderDeliveryRepository;

    @Resource
    private OmsOrderRepository orderRepository;

    @Resource
    private OmsOrderDeliveryTraceService deliveryTraceService;

    @Override
    public CommonResult<List<OmsDeliveryVO>> selcetOrderAndDeliveryList() {
        List<OmsOrderDelivery> deliveries = orderDeliveryRepository.findAll();
        if (deliveries.isEmpty()) {
            return CommonResult.success(List.of(), "No order deliveries found");
        }

        List<Long> orderIds = deliveries.stream().map(OmsOrderDelivery::getOrderId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, OmsOrder> orderMap = orderRepository.findAllById(orderIds).stream()
                .collect(Collectors.toMap(OmsOrder::getId, o -> o, (k1, k2) -> k1));

        List<OmsDeliveryVO> list = deliveries.stream().map(d -> {
            OmsOrder order = orderMap.get(d.getOrderId());
            return new OmsDeliveryVO(
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
                    d.getDeliveryNo()
            );
        }).collect(Collectors.toList());

        return CommonResult.success(list, "Query delivery list successful");
    }

    @Override
    public CommonResult<?> getLogisticsById(Long id) {
        if (id == null) {
            return CommonResult.failed("Invalid logistics ID");
        }

        Optional<OmsOrderDelivery> deliveryOpt = orderDeliveryRepository.findById(id);
        if (deliveryOpt.isEmpty()) {
            return CommonResult.failed("Logistics info not found");
        }

        OmsOrderDelivery delivery = deliveryOpt.get();
        List<OmsOrderDeliveryTrace> traceList = deliveryTraceService.getTracesByDeliveryId(id);

        Map<String, Object> result = new HashMap<>();
        result.put("delivery", delivery);
        result.put("traceList", traceList);

        return CommonResult.success(result, "Logistics details fetched successfully");
    }

    @Override
    public CommonResult<?> search(String orderNo, Integer deliveryStatus) {
        List<OmsOrderDelivery> deliveries = orderDeliveryRepository.findAll();

        if (orderNo != null && !orderNo.isBlank()) {
            deliveries = deliveries.stream()
                    .filter(d -> d.getOrderNo() != null && d.getOrderNo().contains(orderNo))
                    .collect(Collectors.toList());
        }

        if (deliveryStatus != null) {
            deliveries = deliveries.stream()
                    .filter(d -> Objects.equals(d.getDeliveryStatus(), deliveryStatus))
                    .collect(Collectors.toList());
        }

        if (deliveries.isEmpty()) {
            return CommonResult.success(List.of(), "No matching delivery info found");
        }

        List<Long> orderIds = deliveries.stream().map(OmsOrderDelivery::getOrderId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, OmsOrder> orderMap = orderRepository.findAllById(orderIds).stream()
                .collect(Collectors.toMap(OmsOrder::getId, o -> o, (k1, k2) -> k1));

        List<OmsDeliveryVO> list = deliveries.stream().map(d -> {
            OmsOrder order = orderMap.get(d.getOrderId());
            return new OmsDeliveryVO(
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
                    d.getDeliveryNo()
            );
        }).collect(Collectors.toList());

        return CommonResult.success(list, "Search successful");
    }
}
