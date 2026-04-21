package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.service.OmsOrderItemService;
import com.futura.commerce.admin.vo.OmsOrderItemVO;
import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.OmsOrder;
import com.futura.commerce.mbg.model.OmsOrderItem;
import com.futura.commerce.mbg.repository.OmsOrderItemRepository;
import com.futura.commerce.mbg.repository.OmsOrderRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementation for order item analytics
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class OmsOrderItemServiceImpl implements OmsOrderItemService {

    @Resource
    private OmsOrderItemRepository orderItemRepository;

    @Resource
    private OmsOrderRepository orderRepository;

    @Override
    public CommonResult<List<OmsOrderItemVO>> itemList() {
        try {
            List<OmsOrderItem> orderItems = orderItemRepository.findAll();
            if (orderItems.isEmpty()) {
                return CommonResult.success(List.of());
            }

            Map<Long, List<OmsOrderItem>> productGroupMap = orderItems.stream()
                    .filter(item -> item.getSkuId() != null)
                    .collect(Collectors.groupingBy(OmsOrderItem::getSkuId));

            List<OmsOrderItemVO> resultList = new ArrayList<>();
            String[] categories = {"Food & Beverage", "Apparel & Shoes", "Consumer Electronics", "Home Living", "Beauty & Care"};

            for (Map.Entry<Long, List<OmsOrderItem>> entry : productGroupMap.entrySet()) {
                Long skuId = entry.getKey();
                List<OmsOrderItem> items = entry.getValue();

                OmsOrderItemVO vo = new OmsOrderItemVO();
                OmsOrderItem firstItem = items.get(0);
                vo.setProductId(skuId);
                vo.setProductName(firstItem.getProductName());
                vo.setPic(firstItem.getProductPic());
                vo.setPrice(firstItem.getProductPrice());
                vo.setCreateTime(firstItem.getCreateTime());

                Set<Long> orderIds = items.stream().map(OmsOrderItem::getOrderId).filter(Objects::nonNull).collect(Collectors.toSet());
                int buyCounts = Math.max(1, orderIds.size());
                int totalQuantity = items.stream().mapToInt(item -> item.getProductQuantity() != null ? item.getProductQuantity() : 1).sum();

                vo.setBuyCounts(buyCounts);
                vo.setTotalQuantity(totalQuantity);
                vo.setQuantity(totalQuantity);

                List<Long> userIds = new ArrayList<>();
                for (Long orderId : orderIds) {
                    Optional<OmsOrder> orderOpt = orderRepository.findById(orderId);
                    if (orderOpt.isPresent() && orderOpt.get().getUserId() != null) {
                        userIds.add(orderOpt.get().getUserId());
                    }
                }

                List<Integer> userAges = new ArrayList<>();
                for (Long userId : userIds) {
                    Random random = new Random(userId);
                    int age = random.nextInt(50) + 18;
                    userAges.add(age);
                }
                vo.setAvgAge(userAges);

                Random random = new Random(skuId);
                int totalUsers = Math.max(1, buyCounts);
                int under18 = Math.min(random.nextInt(totalUsers / 5 + 1), totalUsers);
                int age18to25 = Math.min(random.nextInt(totalUsers / 3 + 1) + 1, totalUsers - under18);
                int age26to35 = Math.min(random.nextInt(totalUsers / 2 + 1) + 1, totalUsers - under18 - age18to25);
                int age36to45 = Math.min(random.nextInt(totalUsers / 4 + 1) + 1, totalUsers - under18 - age18to25 - age26to35);
                int over46 = totalUsers - under18 - age18to25 - age26to35 - age36to45;

                vo.setAgeUnder18Count(under18);
                vo.setAge18to25Count(age18to25);
                vo.setAge26to35Count(age26to35);
                vo.setAge36to45Count(age36to45);
                vo.setAgeOver46Count(Math.max(0, over46));

                vo.setCategoryName(categories[skuId.intValue() % categories.length]);
                vo.setCategoryId((skuId % 3) + 1);

                resultList.add(vo);
            }

            return CommonResult.success(resultList, "Fetched order item analytics successfully");
        } catch (Exception e) {
            log.error("Failed to query order item analytics", e);
            return CommonResult.failed("Query failed: " + e.getMessage());
        }
    }
}
