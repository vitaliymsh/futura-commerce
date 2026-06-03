package com.futura.commerce.order.service.impl;

import com.futura.commerce.mbg.model.OmsOrderAfterSales;
import com.futura.commerce.mbg.repository.OmsOrderAfterSalesRepository;
import com.futura.commerce.order.service.OmsOrderAfterSalesService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for order after-sales management
 *
 * @author Vitalii
 */
@Service
public class OmsOrderAfterSalesServiceImpl implements OmsOrderAfterSalesService {

    @Resource
    private OmsOrderAfterSalesRepository afterSalesRepository;

    @Resource
    private com.futura.commerce.mbg.repository.PmsProductSkuRepository pmsProductSkuRepository;

    @Override
    public List<OmsOrderAfterSales> findByOrderId(Long orderId) {
        return afterSalesRepository.findByOrderId(orderId);
    }

    @Override
    public Optional<OmsOrderAfterSales> findById(Long id) {
        return afterSalesRepository.findById(id);
    }

    @Override
    public OmsOrderAfterSales save(OmsOrderAfterSales afterSales) {
        return afterSalesRepository.save(afterSales);
    }

    @Override
    public com.futura.commerce.common.api.CommonResult<org.springframework.data.domain.Page<com.futura.commerce.order.dto.AfterOrderDTO>> orderAfterList(Integer page, Integer pageSize) {
        int current = (page == null || page < 1) ? 1 : page;
        int size = (pageSize == null || pageSize < 1) ? 10 : pageSize;

        org.springframework.data.domain.PageRequest pageRequest =
                org.springframework.data.domain.PageRequest.of(current - 1, size, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createTime"));
        org.springframework.data.domain.Page<OmsOrderAfterSales> afterSalesPage = afterSalesRepository.findAll(pageRequest);

        List<OmsOrderAfterSales> records = afterSalesPage.getContent();
        if (records == null || records.isEmpty()) {
            return com.futura.commerce.common.api.CommonResult.success(
                    new org.springframework.data.domain.PageImpl<>(java.util.Collections.emptyList(), pageRequest, 0),
                    "No after-sales records found"
            );
        }

        List<Long> skuIds = records.stream()
                .map(OmsOrderAfterSales::getSkuId)
                .filter(java.util.Objects::nonNull)
                .toList();

        List<com.futura.commerce.mbg.model.PmsProductSku> skuList = pmsProductSkuRepository.findByIdIn(skuIds);
        java.util.Map<Long, com.futura.commerce.mbg.model.PmsProductSku> skuMap = skuList.stream()
                .collect(java.util.stream.Collectors.toMap(com.futura.commerce.mbg.model.PmsProductSku::getId, item -> item, (k1, k2) -> k1));

        List<com.futura.commerce.order.dto.AfterOrderDTO> dtoList = records.stream().map(item -> {
            com.futura.commerce.order.dto.AfterOrderDTO dto = new com.futura.commerce.order.dto.AfterOrderDTO();
            org.springframework.beans.BeanUtils.copyProperties(item, dto);
            com.futura.commerce.mbg.model.PmsProductSku sku = skuMap.get(item.getSkuId());
            if (sku != null) {
                dto.setSpec(sku.getSpec());
                dto.setModel(sku.getModel());
                dto.setPic(sku.getPic());
            }
            return dto;
        }).toList();

        org.springframework.data.domain.Page<com.futura.commerce.order.dto.AfterOrderDTO> finalPage =
                new org.springframework.data.domain.PageImpl<>(dtoList, pageRequest, afterSalesPage.getTotalElements());

        return com.futura.commerce.common.api.CommonResult.success(finalPage, "After-sales records retrieved successfully");
    }
}
