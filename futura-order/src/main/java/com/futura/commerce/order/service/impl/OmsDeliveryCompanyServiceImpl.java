package com.futura.commerce.order.service.impl;

import com.futura.commerce.order.service.OmsDeliveryCompanyService;
import com.futura.commerce.mbg.model.OmsDeliveryCompany;
import com.futura.commerce.mbg.repository.OmsDeliveryCompanyRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for delivery companies
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class OmsDeliveryCompanyServiceImpl implements OmsDeliveryCompanyService {

    @Resource
    private OmsDeliveryCompanyRepository deliveryCompanyRepository;

    @Override
    public List<OmsDeliveryCompany> list() {
        return deliveryCompanyRepository.findByStatusOrderBySortAsc(1);
    }

    @Override
    public OmsDeliveryCompany getById(Integer id) {
        if (id == null) {
            return null;
        }
        return deliveryCompanyRepository.findById(id.longValue()).orElse(null);
    }
}
