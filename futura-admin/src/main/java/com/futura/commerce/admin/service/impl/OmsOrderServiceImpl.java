package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.dto.OmsOrderSearchDTO;
import com.futura.commerce.admin.export.OmsOrderExcel;
import com.futura.commerce.admin.export.OmsOrderImportExcel;
import com.futura.commerce.admin.service.OmsOrderService;
import com.futura.commerce.admin.vo.OmsOrderListVO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

        Optional<OmsOrder> orderOpt = orderRepository.findById(id);
        if (orderOpt.isEmpty()) {
            return CommonResult.notFound();
        }
        OmsOrder order = orderOpt.get();

        List<OmsOrderItem> items = orderItemRepository.findByOrderId(id);
        List<OmsOrderVO.OrderItemVO> itemVOList = new ArrayList<>();
        for (OmsOrderItem item : items) {
            OmsOrderVO.OrderItemVO itemVO = new OmsOrderVO.OrderItemVO();
            itemVO.setProductName(item.getProductName());
            itemVO.setPic(item.getProductPic());
            itemVO.setSpec(item.getProductSkuCode());
            itemVO.setProductPrice(item.getProductPrice() != null ? item.getProductPrice().toString() : null);
            itemVO.setQuantity(item.getProductQuantity());
            itemVO.setRealAmount(item.getRealAmount() != null ? item.getRealAmount().toString() : null);
            itemVOList.add(itemVO);
        }

        Optional<OmsOrderDelivery> deliveryOpt = orderDeliveryRepository.findByOrderId(id);
        OmsOrderVO.DeliveryVO deliveryVO = new OmsOrderVO.DeliveryVO();
        if (deliveryOpt.isPresent()) {
            OmsOrderDelivery d = deliveryOpt.get();
            deliveryVO.setDeliveryCompany(d.getDeliveryNo());
            deliveryVO.setDeliveryNo(d.getDeliveryNo());
            deliveryVO.setDeliveryStatus(d.getDeliveryStatus());
            deliveryVO.setDeliveryTime(d.getDeliveryTime());
            deliveryVO.setSignTime(d.getSignTime());
            deliveryVO.setReceiverName(order.getReceiverName());
            deliveryVO.setReceiverPhone(order.getReceiverPhone());
            deliveryVO.setReceiverAddress(order.getReceiverAddress());
        }

        OmsOrderVO vo = new OmsOrderVO();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setStatus(order.getStatus());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setPayAmount(order.getPayAmount());
        vo.setFreightAmount(order.getFreightAmount());
        vo.setCreateTime(order.getCreateTime());
        vo.setPayTime(order.getPayTime());
        vo.setDeliveryTime(order.getDeliveryTime());
        vo.setReceiveTime(order.getReceiveTime());
        vo.setFinishTime(order.getFinishTime());
        vo.setItems(itemVOList);
        vo.setDelivery(deliveryVO);

        return CommonResult.success(vo, "Order detail queried successfully");
    }

    @Override
    public CommonResult<Page<OmsOrderListVO>> getOrderList(Long pageNum, Long pageSize) {
        int page = (pageNum != null && pageNum > 0) ? pageNum.intValue() - 1 : 0;
        int size = (pageSize != null && pageSize > 0) ? pageSize.intValue() : 10;
        Pageable pageable = PageRequest.of(page, size);

        List<OmsOrder> orderList = orderRepository.findAll();
        List<OmsOrderListVO> voList = convertToOrderListVOs(orderList);

        int total = voList.size();
        int fromIndex = Math.min(page * size, total);
        int toIndex = Math.min(fromIndex + size, total);
        List<OmsOrderListVO> pageContent = voList.subList(fromIndex, toIndex);

        return CommonResult.success(new PageImpl<>(pageContent, pageable, total), "Query order list successful");
    }

    @Override
    public CommonResult<Page<OmsOrderListVO>> search(OmsOrderSearchDTO dto) {
        int page = (dto.getPageNum() != null && dto.getPageNum() > 0) ? dto.getPageNum() - 1 : 0;
        int size = (dto.getPageSize() != null && dto.getPageSize() > 0) ? dto.getPageSize() : 10;
        Pageable pageable = PageRequest.of(page, size);

        List<OmsOrder> orderList = orderRepository.findAll();

        if (dto.getOrderNo() != null && !dto.getOrderNo().trim().isEmpty()) {
            orderList = orderList.stream()
                    .filter(o -> o.getOrderNo() != null && o.getOrderNo().contains(dto.getOrderNo().trim()))
                    .collect(Collectors.toList());
        }

        if (dto.getStatus() != null) {
            orderList = orderList.stream()
                    .filter(o -> Objects.equals(o.getStatus(), dto.getStatus()))
                    .collect(Collectors.toList());
        }

        if (dto.getPayType() != null) {
            orderList = orderList.stream()
                    .filter(o -> Objects.equals(o.getPayType(), dto.getPayType()))
                    .collect(Collectors.toList());
        }

        if (dto.getStartTime() != null && !dto.getStartTime().trim().isEmpty()) {
            try {
                LocalDateTime start = LocalDateTime.parse(dto.getStartTime().trim());
                orderList = orderList.stream()
                        .filter(o -> o.getCreateTime() != null && !o.getCreateTime().isBefore(start))
                        .collect(Collectors.toList());
            } catch (Exception ignored) {}
        }

        if (dto.getEndTime() != null && !dto.getEndTime().trim().isEmpty()) {
            try {
                LocalDateTime end = LocalDateTime.parse(dto.getEndTime().trim());
                orderList = orderList.stream()
                        .filter(o -> o.getCreateTime() != null && !o.getCreateTime().isAfter(end))
                        .collect(Collectors.toList());
            } catch (Exception ignored) {}
        }

        List<OmsOrderListVO> voList = convertToOrderListVOs(orderList);

        if (dto.getBuyerInfo() != null && !dto.getBuyerInfo().trim().isEmpty()) {
            voList = voList.stream()
                    .filter(v -> v.getBuyerInfo() != null && v.getBuyerInfo().contains(dto.getBuyerInfo().trim()))
                    .collect(Collectors.toList());
        }

        if (dto.getMinAmount() != null) {
            BigDecimal min = BigDecimal.valueOf(dto.getMinAmount());
            voList = voList.stream()
                    .filter(v -> v.getTotalAmount() != null && v.getTotalAmount().compareTo(min) >= 0)
                    .collect(Collectors.toList());
        }

        if (dto.getMaxAmount() != null) {
            BigDecimal max = BigDecimal.valueOf(dto.getMaxAmount());
            voList = voList.stream()
                    .filter(v -> v.getTotalAmount() != null && v.getTotalAmount().compareTo(max) <= 0)
                    .collect(Collectors.toList());
        }

        if (dto.getDeliveryStatus() != null) {
            voList = voList.stream()
                    .filter(v -> Objects.equals(v.getDeliveryStatus(), dto.getDeliveryStatus()))
                    .collect(Collectors.toList());
        }

        int total = voList.size();
        int fromIndex = Math.min(page * size, total);
        int toIndex = Math.min(fromIndex + size, total);
        List<OmsOrderListVO> pageContent = voList.subList(fromIndex, toIndex);

        return CommonResult.success(new PageImpl<>(pageContent, pageable, total), "Search orders successful");
    }

    @Override
    public CommonResult<List<OmsOrderExcel>> exportExcel(OmsOrderSearchDTO dto) {
        dto.setPageNum(1);
        dto.setPageSize(Integer.MAX_VALUE);
        CommonResult<Page<OmsOrderListVO>> searchResult = this.search(dto);
        List<OmsOrderListVO> records = searchResult.getData().getContent();
        List<OmsOrderExcel> excelList = records.stream().map(vo -> {
            OmsOrderExcel excel = new OmsOrderExcel();
            BeanUtils.copyProperties(vo, excel);
            return excel;
        }).collect(Collectors.toList());

        return CommonResult.success(excelList, "Export successful");
    }

    @Override
    @Transactional
    public CommonResult<List<OmsOrderListVO>> excelImport(List<OmsOrderImportExcel> list) {
        if (list == null || list.isEmpty()) {
            return CommonResult.failed("No data to import");
        }

        try {
            List<OmsOrderListVO> voList = list.stream().map(excel -> {
                OmsOrderListVO vo = new OmsOrderListVO();
                BeanUtils.copyProperties(excel, vo);
                return vo;
            }).collect(Collectors.toList());

            List<OmsOrder> orderList = voList.stream().map(vo -> {
                OmsOrder order = new OmsOrder();
                BeanUtils.copyProperties(vo, order);

                String buyerInfo = vo.getBuyerInfo();
                if (buyerInfo != null && !buyerInfo.isBlank()) {
                    String[] arr = buyerInfo.split(" ");
                    if (arr.length >= 3) {
                        order.setReceiverName(arr[0]);
                        order.setReceiverPhone(arr[1]);
                        order.setReceiverAddress(arr[2]);
                    }
                }
                if (order.getCreateTime() == null) {
                    order.setCreateTime(LocalDateTime.now());
                }
                return order;
            }).collect(Collectors.toList());

            orderRepository.saveAll(orderList);
            return CommonResult.success(voList, "Import successful");
        } catch (Exception e) {
            log.error("Excel import error", e);
            return CommonResult.failed("Import failed: " + e.getMessage());
        }
    }

    @Override
    public CommonResult<List<OmsOrderListVO>> getOrderByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return CommonResult.success(List.of());
        }

        List<OmsOrder> orderList = orderRepository.findAllById(ids);
        List<OmsOrderListVO> voList = convertToOrderListVOs(orderList);

        return CommonResult.success(voList, "Batch fetch orders successful");
    }

    private List<OmsOrderListVO> convertToOrderListVOs(List<OmsOrder> orderList) {
        if (orderList.isEmpty()) {
            return List.of();
        }

        List<Long> orderIds = orderList.stream().map(OmsOrder::getId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, OmsOrderDelivery> deliveryMap = orderDeliveryRepository.findAll().stream()
                .filter(d -> d.getOrderId() != null && orderIds.contains(d.getOrderId()))
                .collect(Collectors.toMap(OmsOrderDelivery::getOrderId, d -> d, (k1, k2) -> k1));

        return orderList.stream().map(order -> {
            OmsOrderListVO vo = new OmsOrderListVO();
            BeanUtils.copyProperties(order, vo);

            String buyerInfo = (order.getReceiverName() != null ? order.getReceiverName() : "") + " "
                    + (order.getReceiverPhone() != null ? order.getReceiverPhone() : "") + " "
                    + (order.getReceiverAddress() != null ? order.getReceiverAddress() : "");
            vo.setBuyerInfo(buyerInfo.trim());

            OmsOrderDelivery delivery = deliveryMap.get(order.getId());
            if (delivery != null) {
                vo.setDeliveryStatus(delivery.getDeliveryStatus());
                if (vo.getDeliveryTime() == null) {
                    vo.setDeliveryTime(delivery.getDeliveryTime());
                }
            }

            return vo;
        }).collect(Collectors.toList());
    }
}
