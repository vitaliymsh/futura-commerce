package com.futura.commerce.order.service;

import com.futura.commerce.mbg.model.OmsDelivery;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing OmsDelivery
 *
 * @author Vitalii
 */
public interface OmsDeliveryService {

    List<OmsDelivery> findAll();

    Optional<OmsDelivery> findById(Long id);

    OmsDelivery save(OmsDelivery entity);

    void deleteById(Long id);
}
