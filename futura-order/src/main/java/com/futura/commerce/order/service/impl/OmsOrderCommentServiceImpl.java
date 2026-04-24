package com.futura.commerce.order.service.impl;

import com.futura.commerce.mbg.model.OmsOrderComment;
import com.futura.commerce.mbg.repository.OmsOrderCommentRepository;
import com.futura.commerce.order.service.OmsOrderCommentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for order customer comments
 *
 * @author Vitalii
 */
@Service
public class OmsOrderCommentServiceImpl implements OmsOrderCommentService {

    @Resource
    private OmsOrderCommentRepository commentRepository;

    @Override
    public List<OmsOrderComment> findByOrderId(Long orderId) {
        return commentRepository.findByOrderId(orderId);
    }

    @Override
    public List<OmsOrderComment> findByProductId(Long productId) {
        return commentRepository.findByProductId(productId);
    }

    @Override
    public Optional<OmsOrderComment> findById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public OmsOrderComment save(OmsOrderComment comment) {
        return commentRepository.save(comment);
    }
}
