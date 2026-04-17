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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for order operations
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class OmsOrderServiceImpl implements OmsOrderService {

    @Resource
    private OmsOrderRepository orderRepository;

    @Resource
    private OmsOrderDeliveryRepository orderDeliveryRepository;

    @Resource
    private OmsOrderItemRepository orderItemRepository;

    @Override
    public CommonResult<OmsOrderVO> getOrderDetailById(Long id) {
        if (id == null) {
            return CommonResult.failed("Invalid order ID");
        }

        // 1. Fetch order
        Optional<OmsOrder> orderOpt = orderRepository.findById(id);
        if (orderOpt.isEmpty()) {
            return CommonResult.notFound();
        }
        OmsOrder order = orderOpt.get();

        // 2. Fetch order items
        List<OmsOrderItem> items = orderItemRepository.findByOrderId(id);
        List<OmsOrderVO.OrderItemVO> itemVOList = new ArrayList<>();
        for (OmsOrderItem item : items) {
            OmsOrderVO.OrderItemVO itemVO = new OmsOrderVO.OrderItemVO();
            itemVO.setProductName(item.getProductName());
            itemVO.setPic(item.getProductPic());
            itemVO.setSpec(item.getProductSkuCode());
            itemVO.setProductPrice(item.getProductPrice() != null ? item.getProductPrice().toString() : null);
            itemVO.setProductQuantity(item.getProductQuantity());
            if (item.getProductPrice() != null && item.getProductQuantity() != null) {
                itemVO.setProductPriceAmount(item.getProductPrice().multiply(java.math.BigDecimal.valueOf(item.getProductQuantity())).toString());
            }
            itemVOList.add(itemVO);
        }

        // 3. Build VO
        OmsOrderVO vo = new OmsOrderVO();
        BeanUtils.copyProperties(order, vo);
        vo.setOrderItemList(itemVOList);

        // 4. Fetch delivery
        Optional<OmsOrderDelivery> deliveryOpt = orderDeliveryRepository.findByOrderId(id);
        if (deliveryOpt.isPresent()) {
            OmsOrderDelivery delivery = deliveryOpt.get();
            vo.setDeliveryCompany(delivery.getDeliveryCompany());
            vo.setOrderNo(delivery.getOrderNo());
            vo.setDeliveryStatus(delivery.getDeliveryStatus());
            vo.setSignTime(delivery.getSignTime());
            vo.setDeliveryNo(delivery.getDeliveryNo());
        }

        return CommonResult.success(vo, "Order details retrieved successfully");
    }
}
