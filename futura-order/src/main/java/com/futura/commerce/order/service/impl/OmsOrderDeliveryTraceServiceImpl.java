package com.futura.commerce.order.service.impl;

import com.futura.commerce.order.dto.OmsOrderDeliveryTraceSearchDTO;
import com.futura.commerce.order.dto.OmsOrderDeliveryUpdateDTO;
import com.futura.commerce.order.service.OmsDeliveryCompanyService;
import com.futura.commerce.order.service.OmsOrderDeliveryTraceService;
import com.futura.commerce.order.service.SysTableColumnConfigService;
import com.futura.commerce.order.vo.OmsDeliveryAndTraceVO;
import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.OmsDeliveryCompany;
import com.futura.commerce.mbg.model.OmsOrder;
import com.futura.commerce.mbg.model.OmsOrderDelivery;
import com.futura.commerce.mbg.model.OmsOrderDeliveryTrace;
import com.futura.commerce.mbg.repository.OmsDeliveryCompanyRepository;
import com.futura.commerce.mbg.repository.OmsOrderDeliveryRepository;
import com.futura.commerce.mbg.repository.OmsOrderDeliveryTraceRepository;
import com.futura.commerce.order.export.OmsOrderDeliveryTraceExcel;
import com.futura.commerce.mbg.repository.OmsOrderRepository;
import com.futura.commerce.security.dto.LoginUser;
import jakarta.annotation.Resource;
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

    @Resource
    private OmsDeliveryCompanyService deliveryCompanyService;

    @Resource
    private OmsDeliveryCompanyRepository deliveryCompanyRepository;

    @Resource
    private SysTableColumnConfigService sysTableColumnConfigService;

    @Override
    public List<OmsOrderDeliveryTrace> getTracesByDeliveryId(Long deliveryId) {
        if (deliveryId == null) {
            return List.of();
        }
        return deliveryTraceRepository.findByDeliveryId(deliveryId);
    }

    @Override
    public List<OmsDeliveryAndTraceVO> getDeliveryList() {
        List<OmsOrderDelivery> deliveries = orderDeliveryRepository.findAll().stream()
                .filter(d -> d.getDelFlag() == null || d.getDelFlag() == 0)
                .collect(Collectors.toList());
        return convertToVOList(deliveries);
    }

    @Override
    @SuppressWarnings("unchecked")
    public CommonResult<Page<OmsDeliveryAndTraceVO>> getDeliveryList(Integer pageNum, Integer pageSize) {
        int page = (pageNum != null && pageNum > 0) ? pageNum - 1 : 0;
        int size = (pageSize != null && pageSize > 0) ? pageSize : 10;
        Pageable pageable = PageRequest.of(page, size);

        List<OmsOrderDelivery> deliveries = orderDeliveryRepository.findAll().stream()
                .filter(d -> d.getDelFlag() == null || d.getDelFlag() == 0)
                .collect(Collectors.toList());

        List<OmsDeliveryAndTraceVO> list = convertToVOList(deliveries);
        String pageCode = "delivery_trace";
        CommonResult<?> columnResult = sysTableColumnConfigService.showColumn(pageCode);
        List<Map<String, Object>> columns = null;
        if (columnResult.getData() instanceof List<?> rawList) {
            columns = (List<Map<String, Object>>) (List<?>) rawList;
        }

        for (OmsDeliveryAndTraceVO vo : list) {
            vo.setResultList(columns);
        }

        int total = list.size();
        int fromIndex = Math.min(page * size, total);
        int toIndex = Math.min(fromIndex + size, total);
        List<OmsDeliveryAndTraceVO> pageContent = list.subList(fromIndex, toIndex);

        return CommonResult.success(new PageImpl<>(pageContent, pageable, total), "Fetched delivery trace list successfully");
    }

    @Override
    public CommonResult<Map<String, Object>> getLogisticsById(Long id) {
        if (id == null) {
            return CommonResult.failed("Invalid logistics ID");
        }

        Optional<OmsOrderDelivery> deliveryOpt = orderDeliveryRepository.findById(id);
        if (deliveryOpt.isEmpty()) {
            return CommonResult.notFound();
        }

        OmsOrderDelivery delivery = deliveryOpt.get();
        List<OmsOrderDeliveryTrace> traceList = deliveryTraceRepository.findByDeliveryId(id);

        Map<String, Object> result = new HashMap<>();
        result.put("delivery", delivery);
        result.put("traces", traceList);

        return CommonResult.success(result, "Fetched logistics details successfully");
    }

    @Override
    @Transactional
    public CommonResult<?> updateDeliveryTraceInfo(OmsOrderDeliveryUpdateDTO dto) {
        if (dto == null || dto.getOrderId() == null) {
            return CommonResult.failed("Invalid parameters");
        }

        Long adminId = getCurrentAdminId();
        if (adminId == null) {
            return CommonResult.unauthorized("Authentication required");
        }

        Optional<OmsOrderDelivery> deliveryOpt = orderDeliveryRepository.findByOrderId(dto.getOrderId());
        if (deliveryOpt.isEmpty()) {
            return CommonResult.failed("Delivery record not found");
        }

        OmsOrderDelivery delivery = deliveryOpt.get();
        delivery.setDeliveryCompanyId(dto.getDeliveryCompanyId());
        delivery.setDeliveryNo(dto.getDeliveryNo());
        delivery.setDeliveryUser(dto.getDeliveryUser());
        delivery.setDeliveryUserPhone(dto.getDeliveryPhone());
        delivery.setDeliveryStatus(dto.getDeliveryStatus());
        delivery.setRemark(dto.getModifyReason());
        delivery.setUpdateTime(LocalDateTime.now());
        orderDeliveryRepository.save(delivery);

        // Update or append delivery trace
        List<OmsOrderDeliveryTrace> traces = deliveryTraceRepository.findByDeliveryId(delivery.getId());
        OmsOrderDeliveryTrace trace = traces.isEmpty() ? new OmsOrderDeliveryTrace() : traces.get(traces.size() - 1);
        trace.setDeliveryId(delivery.getId());
        trace.setTraceStatus(dto.getTraceStatus());
        trace.setTraceAddress(dto.getTraceAddress());
        trace.setTraceTime(dto.getTraceTime() != null ? dto.getTraceTime() : LocalDateTime.now());
        trace.setOperatorId(adminId.intValue());
        trace.setOperationType(3);
        if (trace.getCreateTime() == null) {
            trace.setCreateTime(LocalDateTime.now());
        }
        deliveryTraceRepository.save(trace);

        // Update order receiver details
        Optional<OmsOrder> orderOpt = orderRepository.findById(dto.getOrderId());
        if (orderOpt.isPresent()) {
            OmsOrder order = orderOpt.get();
            order.setReceiverAddress(dto.getReceiverAddress());
            order.setReceiverName(dto.getReceiverName());
            order.setReceiverPhone(dto.getReceiverPhone());
            order.setRemark(dto.getModifyReason());
            order.setUpdateTime(LocalDateTime.now());
            orderRepository.save(order);
        }

        return CommonResult.success(true, "Updated logistics successfully");
    }

    @Override
    public CommonResult<List<OmsDeliveryAndTraceVO>> searchDto(OmsOrderDeliveryTraceSearchDTO dto) {
        List<OmsOrderDelivery> deliveries = orderDeliveryRepository.findAll().stream()
                .filter(d -> d.getDelFlag() == null || d.getDelFlag() == 0)
                .collect(Collectors.toList());

        if (dto.getDeliveryNo() != null && !dto.getDeliveryNo().trim().isEmpty()) {
            deliveries = deliveries.stream()
                    .filter(d -> d.getDeliveryNo() != null && d.getDeliveryNo().contains(dto.getDeliveryNo().trim()))
                    .collect(Collectors.toList());
        }

        if (dto.getDeliveryCompany() != null) {
            deliveries = deliveries.stream()
                    .filter(d -> Objects.equals(d.getDeliveryCompanyId(), dto.getDeliveryCompany()))
                    .collect(Collectors.toList());
        }

        if (dto.getDeliveryStartTime() != null && !dto.getDeliveryStartTime().trim().isEmpty()) {
            try {
                LocalDateTime start = LocalDateTime.parse(dto.getDeliveryStartTime().trim());
                deliveries = deliveries.stream()
                        .filter(d -> d.getDeliveryTime() != null && !d.getDeliveryTime().isBefore(start))
                        .collect(Collectors.toList());
            } catch (Exception ignored) {}
        }

        if (dto.getDeliveryEndTime() != null && !dto.getDeliveryEndTime().trim().isEmpty()) {
            try {
                LocalDateTime end = LocalDateTime.parse(dto.getDeliveryEndTime().trim());
                deliveries = deliveries.stream()
                        .filter(d -> d.getDeliveryTime() != null && !d.getDeliveryTime().isAfter(end))
                        .collect(Collectors.toList());
            } catch (Exception ignored) {}
        }

        if (deliveries.isEmpty()) {
            return CommonResult.success(List.of(), "No matching delivery records found");
        }

        List<OmsDeliveryAndTraceVO> resultList = convertToVOList(deliveries).stream()
                .filter(vo -> matchesFilters(vo, dto))
                .collect(Collectors.toList());

        return CommonResult.success(resultList, "Search successful");
    }

    @Override
    public CommonResult<?> setDeliveryTraceColumn(List<Map<String, Object>> list) {
        String pageCode = "delivery_trace";
        return sysTableColumnConfigService.updateColumnConfig(list, pageCode);
    }

    @Override
    public CommonResult<List<OmsOrderDeliveryTraceExcel>> exportExcel(OmsOrderDeliveryTraceSearchDTO dto) {
        CommonResult<List<OmsDeliveryAndTraceVO>> searchResult = this.searchDto(dto);
        List<OmsDeliveryAndTraceVO> records = searchResult.getData();
        List<OmsOrderDeliveryTraceExcel> collect = records.stream().map(vo -> {
            OmsOrderDeliveryTraceExcel excel = new OmsOrderDeliveryTraceExcel();
            excel.setOrderNo(vo.getOrderNo());
            excel.setDeliveryNo(vo.getDeliveryNo());
            excel.setReceiverName(vo.getReceiverName());
            excel.setReceiverPhone(vo.getReceiverPhone());
            excel.setDeliveryCompany(vo.getDeliveryCompanyId());
            excel.setTraceStatus(vo.getTraceStatus());
            if (vo.getDeliveryTime() != null) {
                excel.setDeliveryStartTime(vo.getDeliveryTime().toString());
                excel.setDeliveryEndTime(vo.getDeliveryTime().toString());
            }
            if (vo.getTraceTime() != null) {
                excel.setTraceStartTime(vo.getTraceTime().toString());
                excel.setTraceEndTime(vo.getTraceTime().toString());
            }
            return excel;
        }).collect(Collectors.toList());

        return CommonResult.success(collect, "Export delivery traces successful");
    }

    private List<OmsDeliveryAndTraceVO> convertToVOList(List<OmsOrderDelivery> deliveries) {
        if (deliveries.isEmpty()) {
            return List.of();
        }

        List<Long> orderIds = deliveries.stream().map(OmsOrderDelivery::getOrderId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, OmsOrder> orderMap = orderRepository.findAllById(orderIds).stream()
                .collect(Collectors.toMap(OmsOrder::getId, o -> o, (k1, k2) -> k1));

        Map<Long, String> companyMap = deliveryCompanyRepository.findAll().stream()
                .collect(Collectors.toMap(OmsDeliveryCompany::getId, OmsDeliveryCompany::getDeliveryCompany, (k1, k2) -> k1));

        return deliveries.stream().map(d -> {
            OmsOrder order = orderMap.get(d.getOrderId());
            String companyName = d.getDeliveryCompanyId() != null ? companyMap.get(d.getDeliveryCompanyId().longValue()) : null;

            OmsOrderDeliveryTrace latestTrace = deliveryTraceRepository.findByDeliveryId(d.getId()).stream()
                    .max(Comparator.comparing(OmsOrderDeliveryTrace::getTraceTime, Comparator.nullsFirst(Comparator.naturalOrder())))
                    .orElse(null);

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
            if (latestTrace != null) {
                vo.setTraceStatus(latestTrace.getTraceStatus());
                vo.setSort(latestTrace.getSort());
                vo.setTraceTime(latestTrace.getTraceTime());
            }
            return vo;
        }).collect(Collectors.toList());
    }

    private boolean matchesFilters(OmsDeliveryAndTraceVO vo, OmsOrderDeliveryTraceSearchDTO dto) {
        if (dto.getOrderNo() != null && !dto.getOrderNo().trim().isEmpty()) {
            if (vo.getOrderNo() == null || !vo.getOrderNo().contains(dto.getOrderNo().trim())) {
                return false;
            }
        }

        if (dto.getTraceStatus() != null && !Objects.equals(dto.getTraceStatus(), vo.getTraceStatus())) {
            return false;
        }

        if (dto.getReceiverName() != null && !dto.getReceiverName().trim().isEmpty()) {
            if (vo.getReceiverName() == null || !vo.getReceiverName().contains(dto.getReceiverName().trim())) {
                return false;
            }
        }

        if (dto.getReceiverPhone() != null && !dto.getReceiverPhone().trim().isEmpty()) {
            if (vo.getReceiverPhone() == null || !vo.getReceiverPhone().contains(dto.getReceiverPhone().trim())) {
                return false;
            }
        }

        if (vo.getTraceTime() != null) {
            if (dto.getTraceStartTime() != null && !dto.getTraceStartTime().trim().isEmpty()) {
                try {
                    LocalDateTime startTime = LocalDateTime.parse(dto.getTraceStartTime().trim());
                    if (vo.getTraceTime().isBefore(startTime)) {
                        return false;
                    }
                } catch (Exception ignored) {}
            }
            if (dto.getTraceEndTime() != null && !dto.getTraceEndTime().trim().isEmpty()) {
                try {
                    LocalDateTime endTime = LocalDateTime.parse(dto.getTraceEndTime().trim());
                    if (vo.getTraceTime().isAfter(endTime)) {
                        return false;
                    }
                } catch (Exception ignored) {}
            }
        } else if ((dto.getTraceStartTime() != null && !dto.getTraceStartTime().trim().isEmpty()) ||
                (dto.getTraceEndTime() != null && !dto.getTraceEndTime().trim().isEmpty())) {
            return false;
        }

        return true;
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
