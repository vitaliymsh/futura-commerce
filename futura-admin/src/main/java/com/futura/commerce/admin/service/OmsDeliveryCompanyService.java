package com.futura.commerce.admin.service;

import com.futura.commerce.mbg.model.OmsDeliveryCompany;

import java.util.List;

/**
 * Service interface for delivery companies
 *
 * @author Vitalii
 */
public interface OmsDeliveryCompanyService {

    List<OmsDeliveryCompany> list();

    OmsDeliveryCompany getById(Integer id);
}
