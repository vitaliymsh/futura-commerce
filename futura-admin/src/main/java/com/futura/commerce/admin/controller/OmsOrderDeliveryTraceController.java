package com.futura.commerce.admin.controller;

import com.futura.commerce.admin.dto.OmsOrderDeliveryUpdateDTO;
import com.futura.commerce.admin.service.OmsOrderDeliveryTraceService;
import com.futura.commerce.admin.vo.OmsDeliveryAndTraceVO;
import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.OmsOrder;
import com.futura.commerce.mbg.model.OmsOrderDelivery;
import com.futura.commerce.mbg.repository.OmsOrderDeliveryRepository;
import com.futura.commerce.mbg.repository.OmsOrderRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Order delivery trace tracking controller
 *
 * @author Vitalii
 */
@Slf4j
@RestController
@RequestMapping("/deliveryTrace")
@Tag(name = "DeliveryTraceController", description = "Order delivery trace management")
public class OmsOrderDeliveryTraceController {

    @Resource
    private OmsOrderDeliveryTraceService omsOrderDeliveryTraceService;

    @Resource
    private OmsOrderDeliveryRepository orderDeliveryRepository;

    @Resource
    private OmsOrderRepository orderRepository;

    /**
     * Get logistics delivery trace list
     */
    @Operation(summary = "Get delivery trace list", description = "Retrieve list of all deliveries and tracking info")
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','delivery:view')")
    public CommonResult<List<OmsDeliveryAndTraceVO>> list() {
        List<OmsDeliveryAndTraceVO> list = omsOrderDeliveryTraceService.getDeliveryList();
        return CommonResult.success(list, "Fetched delivery trace list successfully");
    }

    /**
     * Update order delivery logistics information
     */
    @Operation(summary = "Update order delivery info", description = "Update delivery courier, number, and receiver details")
    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','delivery:edit')")
    @Transactional
    public CommonResult<?> update(@RequestBody OmsOrderDeliveryUpdateDTO dto) {
        if (dto == null || dto.getOrderId() == null) {
            return CommonResult.failed("Invalid parameters");
        }

        // 1. Find delivery record
        Optional<OmsOrderDelivery> deliveryOpt = orderDeliveryRepository.findByOrderId(dto.getOrderId());
        if (deliveryOpt.isEmpty()) {
            return CommonResult.notFound();
        }
        OmsOrderDelivery delivery = deliveryOpt.get();

        // 2. Update delivery record
        delivery.setDeliveryCompany(dto.getDeliveryCompany());
        delivery.setDeliveryNo(dto.getDeliveryNo());
        delivery.setDeliveryUser(dto.getDeliveryUser());
        delivery.setDeliveryUserPhone(dto.getDeliveryPhone());
        delivery.setDeliveryStatus(dto.getDeliveryStatus());
        delivery.setRemark(dto.getModifyReason());
        delivery.setUpdateTime(LocalDateTime.now());
        orderDeliveryRepository.save(delivery);

        // 3. Find order
        Optional<OmsOrder> orderOpt = orderRepository.findById(dto.getOrderId());
        if (orderOpt.isEmpty()) {
            return CommonResult.notFound();
        }
        OmsOrder order = orderOpt.get();

        // 4. Update order details
        order.setReceiverAddress(dto.getReceiverAddress());
        order.setReceiverName(dto.getReceiverName());
        order.setReceiverPhone(dto.getReceiverPhone());
        order.setRemark(dto.getModifyReason());
        orderRepository.save(order);

        return CommonResult.success(null, "Logistics updated successfully");
    }
}
