package com.futura.commerce.order.service;

import com.futura.commerce.mbg.model.OmsOrderFinance;

import java.util.Optional;

/**
 * Service interface for order finance records
 *
 * @author Vitalii
 */
public interface OmsOrderFinanceService {
    Optional<OmsOrderFinance> findByOrderId(Long orderId);
    Optional<OmsOrderFinance> findByOrderNo(String orderNo);
    OmsOrderFinance save(OmsOrderFinance finance);
}
