package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.service.OmsOrderService;
import com.futura.commerce.mbg.model.OmsOrder;
import com.futura.commerce.mbg.repository.OmsOrderRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing OmsOrder
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class OmsOrderServiceImpl implements OmsOrderService {

    @Resource
    private OmsOrderRepository omsOrderRepository;

    @Override
    public List<OmsOrder> findAll() {
        return omsOrderRepository.findAll();
    }

    @Override
    public Optional<OmsOrder> findById(Long id) {
        return omsOrderRepository.findById(id);
    }

    @Override
    public OmsOrder save(OmsOrder entity) {
        return omsOrderRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        omsOrderRepository.deleteById(id);
    }
}
