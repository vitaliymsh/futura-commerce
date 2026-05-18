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
    com.futura.commerce.common.api.CommonResult<List<com.futura.commerce.order.dto.OrderCommentDTO>> orderComment(Long productId);
    com.futura.commerce.common.api.CommonResult<org.springframework.data.domain.Page<com.futura.commerce.order.dto.AdminCommentDTO>> adminCommentList(Integer page, Integer pageSize);
    com.futura.commerce.common.api.CommonResult<com.futura.commerce.order.dto.OrderCommentDTO> saveComment(com.futura.commerce.order.dto.OrderCommentDTO orderCommentDTO);
}
