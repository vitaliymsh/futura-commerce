package com.futura.commerce.order.service.impl;

import com.futura.commerce.mbg.model.OmsOrderFinance;
import com.futura.commerce.mbg.repository.OmsOrderFinanceRepository;
import com.futura.commerce.order.service.OmsOrderFinanceService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service implementation for order finance records
 *
 * @author Vitalii
 */
@Service
public class OmsOrderFinanceServiceImpl implements OmsOrderFinanceService {

    @Resource
    private OmsOrderFinanceRepository financeRepository;

    @Override
    public Optional<OmsOrderFinance> findByOrderId(Long orderId) {
        return financeRepository.findByOrderId(orderId);
    }

    @Override
    public Optional<OmsOrderFinance> findByOrderNo(String orderNo) {
        return financeRepository.findByOrderNo(orderNo);
    }

    @Override
    public OmsOrderFinance save(OmsOrderFinance finance) {
        return financeRepository.save(finance);
    }
}
