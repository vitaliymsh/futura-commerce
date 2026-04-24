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
}
