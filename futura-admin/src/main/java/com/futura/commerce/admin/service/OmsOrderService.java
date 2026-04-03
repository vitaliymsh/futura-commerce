package com.futura.commerce.admin.service;

import com.futura.commerce.mbg.model.OmsOrder;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing OmsOrder
 *
 * @author Vitalii
 */
public interface OmsOrderService {

    List<OmsOrder> findAll();

    Optional<OmsOrder> findById(Long id);

    OmsOrder save(OmsOrder entity);

    void deleteById(Long id);
}
