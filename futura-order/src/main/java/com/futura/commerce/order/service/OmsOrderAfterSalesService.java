package com.futura.commerce.order.service;

import com.futura.commerce.mbg.model.OmsOrderAfterSales;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for order after-sales management
 *
 * @author Vitalii
 */
public interface OmsOrderAfterSalesService {
    List<OmsOrderAfterSales> findByOrderId(Long orderId);
    Optional<OmsOrderAfterSales> findById(Long id);
    OmsOrderAfterSales save(OmsOrderAfterSales afterSales);
    com.futura.commerce.common.api.CommonResult<org.springframework.data.domain.Page<com.futura.commerce.order.dto.AfterOrderDTO>> orderAfterList(Integer page, Integer pageSize);
}
