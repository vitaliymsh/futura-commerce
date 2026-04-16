package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.service.OmsOrderService;
import com.futura.commerce.admin.vo.OmsOrderVO;
import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.OmsOrder;
import com.futura.commerce.mbg.model.OmsOrderDelivery;
import com.futura.commerce.mbg.model.OmsOrderItem;
import com.futura.commerce.mbg.repository.OmsOrderDeliveryRepository;
import com.futura.commerce.mbg.repository.OmsOrderItemRepository;
import com.futura.commerce.mbg.repository.OmsOrderRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for order operations
 *
 * @author Vitalii
 */
@Service
public class OmsOrderServiceImpl implements OmsOrderService {

    @Resource
    private OmsOrderRepository orderRepository;

    @Resource
    private OmsOrderDeliveryRepository orderDeliveryRepository;

    @Resource
    private OmsOrderItemRepository orderItemRepository;

    @Override
    public CommonResult<OmsOrderVO> orderDetail(Long id) {
        if (id == null) {
            return CommonResult.failed("Invalid order ID");
        }

        Optional<OmsOrder> orderOpt = orderRepository.findById(id);
        if (orderOpt.isEmpty()) {
            return CommonResult.failed("Order not found");
        }

        OmsOrderVO vo = new OmsOrderVO();
        vo.setOrder(orderOpt.get());

        Optional<OmsOrderDelivery> deliveryOpt = orderDeliveryRepository.findByOrderId(id);
        deliveryOpt.ifPresent(vo::setDelivery);

        List<OmsOrderItem> items = orderItemRepository.findByOrderId(id);
        vo.setOrderItemList(items);

        return CommonResult.success(vo, "Order details fetched successfully");
    }
}
