package com.futura.commerce.order.service;

import com.futura.commerce.mbg.model.OmsOrderComment;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for order customer comments
 *
 * @author Vitalii
 */
public interface OmsOrderCommentService {
    List<OmsOrderComment> findByOrderId(Long orderId);
    List<OmsOrderComment> findByProductId(Long productId);
    Optional<OmsOrderComment> findById(Long id);
    OmsOrderComment save(OmsOrderComment comment);
}
