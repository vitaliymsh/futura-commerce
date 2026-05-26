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

    @Override
    public com.futura.commerce.common.api.CommonResult<List<com.futura.commerce.order.dto.OrderCommentDTO>> orderComment(Long productId) {
        if (productId == null) {
            return com.futura.commerce.common.api.CommonResult.success(java.util.Collections.emptyList(), "No comments found");
        }
        List<OmsOrderComment> commentList = commentRepository.findByProductIdOrderByCommentTimeDesc(productId);
        if (commentList == null || commentList.isEmpty()) {
            return com.futura.commerce.common.api.CommonResult.success(java.util.Collections.emptyList(), "No comments found");
        }
        List<com.futura.commerce.order.dto.OrderCommentDTO> dtoList = commentList.stream()
                .map(comment -> {
                    com.futura.commerce.order.dto.OrderCommentDTO dto = new com.futura.commerce.order.dto.OrderCommentDTO();
                    org.springframework.beans.BeanUtils.copyProperties(comment, dto);
                    return dto;
                })
                .toList();
        return com.futura.commerce.common.api.CommonResult.success(dtoList, "Comments retrieved successfully");
    }
}
