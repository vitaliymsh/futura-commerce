package com.futura.commerce.order.service.impl;

import com.futura.commerce.order.dto.DeliveryShipDTO;
import com.futura.commerce.order.dto.OmsOrderDeliveryCancelDTO;
import com.futura.commerce.order.dto.OmsOrderDeliverySearchDTO;
import com.futura.commerce.order.dto.UpdateTrackingNoDTO;
import com.futura.commerce.order.service.OmsDeliveryCompanyService;
import com.futura.commerce.order.service.OmsOrderDeliveryService;
import com.futura.commerce.order.service.OmsOrderDeliveryTraceService;
import com.futura.commerce.order.vo.OmsDeliveryAndTraceVO;
import com.futura.commerce.order.vo.OmsDeliveryVO;
import com.futura.commerce.common.annotation.OperationLog;
import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.OmsDeliveryCompany;
import com.futura.commerce.mbg.model.OmsOrder;
import com.futura.commerce.mbg.model.OmsOrderDelivery;
import com.futura.commerce.mbg.model.OmsOrderDeliveryTrace;
import com.futura.commerce.mbg.repository.OmsDeliveryCompanyRepository;
import com.futura.commerce.mbg.repository.OmsOrderDeliveryRepository;
import com.futura.commerce.mbg.repository.OmsOrderRepository;
import com.futura.commerce.security.dto.LoginUser;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Resource
    private OmsDeliveryCompanyService deliveryCompanyService;

    @Resource
    private OmsDeliveryCompanyRepository deliveryCompanyRepository;

    @Override
    public CommonResult<List<OmsDeliveryVO>> selcetOrderAndDeliveryList() {
        List<OmsOrderDelivery> deliveries = orderDeliveryRepository.findAll();
        if (deliveries.isEmpty()) {
            return CommonResult.success(List.of(), "No order deliveries found");
        }

        List<Long> orderIds = deliveries.stream().map(OmsOrderDelivery::getOrderId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, OmsOrder> orderMap = new HashMap<>();
        for (OmsOrder o : orderRepository.findAllById(orderIds)) {
            if (o.getId() != null) {
                orderMap.put(o.getId(), o);
            }
        }

        Map<Long, String> companyMap = getCompanyMap();

        List<OmsDeliveryVO> list = deliveries.stream().map(d -> {
            OmsOrder order = orderMap.get(d.getOrderId());
            String companyName = d.getDeliveryCompanyId() != null ? companyMap.get(d.getDeliveryCompanyId().longValue()) : null;
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
                    companyName,
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
            return CommonResult.notFound();
        }

        OmsOrderDelivery delivery = deliveryOpt.get();
        List<OmsOrderDeliveryTrace> traceList = deliveryTraceService.getTracesByDeliveryId(id);

        Map<String, Object> result = new HashMap<>();
        result.put("delivery", delivery);
        result.put("traces", traceList);

        return CommonResult.success(result, "Get logistics details successful");
    }

    @Override
    public CommonResult<?> search(String orderNo, Integer deliveryStatus) {
        List<OmsOrderDelivery> deliveries = orderDeliveryRepository.findAll();

        if (orderNo != null && !orderNo.trim().isEmpty()) {
            deliveries = deliveries.stream()
                    .filter(d -> d.getOrderNo() != null && d.getOrderNo().contains(orderNo.trim()))
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

        Map<Long, String> companyMap = getCompanyMap();

        List<OmsDeliveryVO> list = deliveries.stream().map(d -> {
            OmsOrder order = orderMap.get(d.getOrderId());
            String companyName = d.getDeliveryCompanyId() != null ? companyMap.get(d.getDeliveryCompanyId().longValue()) : null;
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
                    companyName,
                    d.getDeliveryNo()
            );
        }).collect(Collectors.toList());

        return CommonResult.success(list, "Search successful");
    }

    @Override
    public CommonResult<Page<OmsDeliveryAndTraceVO>> selectOrderAndDeliveryList(Integer page, Integer pageSize, Integer[] deliveryStatus) {
        int pageNum = (page != null && page > 0) ? page - 1 : 0;
        int size = (pageSize != null && pageSize > 0) ? pageSize : 10;
        Pageable pageable = PageRequest.of(pageNum, size);

        List<OmsOrderDelivery> allDeliveries = orderDeliveryRepository.findAll().stream()
                .filter(d -> d.getDelFlag() == null || d.getDelFlag() == 0)
                .collect(Collectors.toList());

        if (deliveryStatus != null && deliveryStatus.length > 0) {
            Set<Integer> statusSet = Arrays.stream(deliveryStatus).collect(Collectors.toSet());
            allDeliveries = allDeliveries.stream()
                    .filter(d -> d.getDeliveryStatus() != null && statusSet.contains(d.getDeliveryStatus()))
                    .collect(Collectors.toList());
        }

        if (allDeliveries.isEmpty()) {
            return CommonResult.success(new PageImpl<>(List.of(), pageable, 0), "No delivery records found");
        }

        List<OmsDeliveryAndTraceVO> voList = convertToDeliveryAndTraceVOs(allDeliveries);
        int total = voList.size();
        int fromIndex = Math.min(pageNum * size, total);
        int toIndex = Math.min(fromIndex + size, total);
        List<OmsDeliveryAndTraceVO> pageContent = voList.subList(fromIndex, toIndex);

        return CommonResult.success(new PageImpl<>(pageContent, pageable, total), "Query delivery list successful");
    }

    @Override
    public CommonResult<Page<OmsDeliveryAndTraceVO>> search(OmsOrderDeliverySearchDTO dto) {
        int pageNum = (dto.getPage() != null && dto.getPage() > 0) ? dto.getPage() - 1 : 0;
        int size = (dto.getPageSize() != null && dto.getPageSize() > 0) ? dto.getPageSize() : 10;
        Pageable pageable = PageRequest.of(pageNum, size);

        List<OmsOrderDelivery> allDeliveries = orderDeliveryRepository.findAll().stream()
                .filter(d -> d.getDelFlag() == null || d.getDelFlag() == 0)
                .collect(Collectors.toList());

        if (dto.getDeliveryStatus() != null) {
            allDeliveries = allDeliveries.stream()
                    .filter(d -> Objects.equals(d.getDeliveryStatus(), dto.getDeliveryStatus()))
                    .collect(Collectors.toList());
        }

        if (dto.getDeliveryNo() != null && !dto.getDeliveryNo().trim().isEmpty()) {
            allDeliveries = allDeliveries.stream()
                    .filter(d -> d.getDeliveryNo() != null && d.getDeliveryNo().contains(dto.getDeliveryNo().trim()))
                    .collect(Collectors.toList());
        }

        List<OmsDeliveryAndTraceVO> voList = convertToDeliveryAndTraceVOs(allDeliveries);

        if (dto.getOrderNo() != null && !dto.getOrderNo().trim().isEmpty()) {
            voList = voList.stream()
                    .filter(v -> v.getOrderNo() != null && v.getOrderNo().contains(dto.getOrderNo().trim()))
                    .collect(Collectors.toList());
        }

        if (dto.getReceiverPhone() != null && !dto.getReceiverPhone().trim().isEmpty()) {
            voList = voList.stream()
                    .filter(v -> v.getReceiverPhone() != null && v.getReceiverPhone().contains(dto.getReceiverPhone().trim()))
                    .collect(Collectors.toList());
        }

        int total = voList.size();
        int fromIndex = Math.min(pageNum * size, total);
        int toIndex = Math.min(fromIndex + size, total);
        List<OmsDeliveryAndTraceVO> pageContent = voList.subList(fromIndex, toIndex);

        String msg = pageContent.isEmpty() ? "No matching records" : "Search successful";
        return CommonResult.success(new PageImpl<>(pageContent, pageable, total), msg);
    }

    @Override
    public CommonResult<List<OmsDeliveryCompany>> getDeliveryCompanyList() {
        List<OmsDeliveryCompany> list = deliveryCompanyService.list();
        return CommonResult.success(list, "Fetched delivery company list successfully");
    }

    @Override
    public CommonResult<Map<String, Long>> selectStatus() {
        List<OmsOrderDelivery> list = orderDeliveryRepository.findAll().stream()
                .filter(d -> d.getDelFlag() == null || d.getDelFlag() == 0)
                .collect(Collectors.toList());

        long waitSend = list.stream().filter(d -> d.getDeliveryStatus() != null && d.getDeliveryStatus() == 0).count();
        long delivering = list.stream().filter(d -> d.getDeliveryStatus() != null && (d.getDeliveryStatus() == 1 || d.getDeliveryStatus() == 2)).count();
        long finish = list.stream().filter(d -> d.getDeliveryStatus() != null && d.getDeliveryStatus() == 3).count();
        long allTotal = list.size();

        Map<String, Long> map = new HashMap<>();
        map.put("allTotal", allTotal);
        map.put("waitSend", waitSend);
        map.put("delivering", delivering);
        map.put("finish", finish);

        return CommonResult.success(map, "Fetched delivery status statistics successfully");
    }

    @Override
    @Transactional
    @OperationLog(module = "Delivery", operationType = "UPDATE_STATUS", content = "Ship order", businessIdParam = "orderId")
    public CommonResult<DeliveryShipDTO> ship(Long orderId, DeliveryShipDTO dto) {
        Long adminId = getCurrentAdminId();
        if (adminId == null) {
            return CommonResult.unauthorized("Authentication required");
        }

        Optional<OmsOrderDelivery> deliveryOpt = orderDeliveryRepository.findById(orderId);
        if (deliveryOpt.isEmpty()) {
            return CommonResult.failed("Delivery record not found");
        }

        OmsOrderDelivery delivery = deliveryOpt.get();
        delivery.setDeliveryCompanyId(dto.getDeliveryCompanyId());
        delivery.setDeliveryUser(dto.getDeliveryUser());
        delivery.setDeliveryNo(dto.getTrackingNo());
        delivery.setOperator(dto.getOperator());
        delivery.setDeliveryUserPhone(dto.getPhone());
        delivery.setDeliveryStatus(1);
        delivery.setRemark(dto.getRemark());
        delivery.setUpdateTime(LocalDateTime.now());
        orderDeliveryRepository.save(delivery);

        Optional<OmsOrder> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            OmsOrder order = orderOpt.get();
            order.setStatus(2);
            order.setDeliveryTime(LocalDateTime.now());
            order.setUpdateTime(LocalDateTime.now());
            orderRepository.save(order);
        }

        return CommonResult.success(dto, "Shipment dispatched successfully");
    }

    @Override
    @Transactional
    @OperationLog(module = "Delivery", operationType = "UPDATE", content = "Update tracking number", businessIdParam = "orderId")
    public CommonResult<?> updateTrackingNo(Long orderId, UpdateTrackingNoDTO dto) {
        Optional<OmsOrderDelivery> deliveryOpt = orderDeliveryRepository.findById(orderId);
        if (deliveryOpt.isEmpty()) {
            return CommonResult.notFound();
        }

        OmsOrderDelivery delivery = deliveryOpt.get();
        if (delivery.getDeliveryNo() != null && !delivery.getDeliveryNo().equals(dto.getOldTrackingNo())) {
            return CommonResult.failed("Old tracking number mismatch");
        }

        log.info("Updating tracking number for order: {}, old: {}, new: {}", orderId, dto.getOldTrackingNo(), dto.getNewTrackingNo());
        delivery.setDeliveryNo(dto.getNewTrackingNo());
        delivery.setOperator(dto.getOperator());
        delivery.setRemark(dto.getReason());
        delivery.setUpdateTime(LocalDateTime.now());
        orderDeliveryRepository.save(delivery);

        return CommonResult.success(null, "Tracking number updated successfully");
    }

    @Override
    @Transactional
    public CommonResult<?> cancelDelivery(Long orderId, OmsOrderDeliveryCancelDTO dto) {
        Optional<OmsOrderDelivery> deliveryOpt = orderDeliveryRepository.findByOrderId(orderId);
        if (deliveryOpt.isEmpty()) {
            return CommonResult.notFound();
        }

        OmsOrderDelivery delivery = deliveryOpt.get();
        delivery.setDelFlag(1);
        if (dto != null) {
            if (dto.getRemark() != null) {
                delivery.setRemark(dto.getRemark());
            }
            if (dto.getOperator() != null) {
                delivery.setOperator(dto.getOperator());
            }
        }
        delivery.setUpdateTime(LocalDateTime.now());
        orderDeliveryRepository.save(delivery);

        return CommonResult.success("Cancelled delivery successfully");
    }

    private Map<Long, String> getCompanyMap() {
        Map<Long, String> map = new HashMap<>();
        for (OmsDeliveryCompany company : deliveryCompanyRepository.findAll()) {
            if (company.getId() != null) {
                map.put(company.getId(), company.getDeliveryCompany() != null ? company.getDeliveryCompany() : "DHL Express");
            }
        }
        return map;
    }

    private List<OmsDeliveryAndTraceVO> convertToDeliveryAndTraceVOs(List<OmsOrderDelivery> deliveries) {
        if (deliveries.isEmpty()) {
            return List.of();
        }

        List<Long> orderIds = deliveries.stream().map(OmsOrderDelivery::getOrderId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, OmsOrder> orderMap = new HashMap<>();
        for (OmsOrder o : orderRepository.findAllById(orderIds)) {
            if (o.getId() != null) {
                orderMap.put(o.getId(), o);
            }
        }

        Map<Long, String> companyMap = getCompanyMap();

        return deliveries.stream().map(d -> {
            OmsOrder order = orderMap.get(d.getOrderId());
            String companyName = d.getDeliveryCompanyId() != null ? companyMap.get(d.getDeliveryCompanyId().longValue()) : null;

            OmsDeliveryAndTraceVO vo = new OmsDeliveryAndTraceVO();
            vo.setOrderId(d.getOrderId());
            vo.setOrderNo(d.getOrderNo() != null ? d.getOrderNo() : (order != null ? order.getOrderNo() : null));
            vo.setReceiverName(order != null ? order.getReceiverName() : null);
            vo.setReceiverPhone(order != null ? order.getReceiverPhone() : null);
            vo.setReceiverAddress(order != null ? order.getReceiverAddress() : null);
            vo.setDeliveryStatus(d.getDeliveryStatus());
            vo.setSignTime(d.getSignTime());
            vo.setDeliveryTime(d.getDeliveryTime());
            vo.setDeliveryUser(d.getDeliveryUser());
            vo.setDeliveryCompanyId(d.getDeliveryCompanyId());
            vo.setDeliveryCompany(companyName);
            vo.setDeliveryNo(d.getDeliveryNo());
            vo.setOperator(d.getOperator());
            vo.setUpdateTime(d.getUpdateTime());
            vo.setDelFlag(d.getDelFlag());
            return vo;
        }).collect(Collectors.toList());
    }

    private Long getCurrentAdminId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser loginUser) {
            if (loginUser.getAdmin() != null) {
                return loginUser.getAdmin().getId();
            }
        }
        return 1L;
    }
}
