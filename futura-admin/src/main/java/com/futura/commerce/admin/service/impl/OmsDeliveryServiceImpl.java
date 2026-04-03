package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.service.OmsDeliveryService;
import com.futura.commerce.mbg.model.OmsDelivery;
import com.futura.commerce.mbg.repository.OmsDeliveryRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing OmsDelivery
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class OmsDeliveryServiceImpl implements OmsDeliveryService {

    @Resource
    private OmsDeliveryRepository omsDeliveryRepository;

    @Override
    public List<OmsDelivery> findAll() {
        return omsDeliveryRepository.findAll();
    }

    @Override
    public Optional<OmsDelivery> findById(Long id) {
        return omsDeliveryRepository.findById(id);
    }

    @Override
    public OmsDelivery save(OmsDelivery entity) {
        return omsDeliveryRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        omsDeliveryRepository.deleteById(id);
    }
}
