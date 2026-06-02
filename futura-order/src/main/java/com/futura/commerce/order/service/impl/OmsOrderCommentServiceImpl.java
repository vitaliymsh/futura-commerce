package com.futura.commerce.order.service.impl;

import com.futura.commerce.mbg.model.CommentTagRelation;
import com.futura.commerce.mbg.model.OmsOrderComment;
import com.futura.commerce.mbg.model.ProductCommentImage;
import com.futura.commerce.mbg.repository.CommentTagRelationRepository;
import com.futura.commerce.mbg.repository.OmsOrderCommentRepository;
import com.futura.commerce.mbg.repository.ProductCommentImageRepository;
import com.futura.commerce.order.dto.AdminCommentDTO;
import com.futura.commerce.order.service.OmsOrderCommentService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementation for order customer comments
 *
 * @author Vitalii
 */
@Service
public class OmsOrderCommentServiceImpl implements OmsOrderCommentService {

    @Resource
    private OmsOrderCommentRepository commentRepository;

    @Resource
    private ProductCommentImageRepository productCommentImageRepository;

    @Resource
    private CommentTagRelationRepository commentTagRelationRepository;

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

    @Override
    public com.futura.commerce.common.api.CommonResult<Page<AdminCommentDTO>> adminCommentList(Integer page, Integer pageSize) {
        int current = (page == null || page < 1) ? 1 : page;
        int size = (pageSize == null || pageSize < 1) ? 10 : pageSize;

        PageRequest pageRequest = PageRequest.of(current - 1, size, Sort.by(Sort.Direction.DESC, "commentTime"));
        Page<OmsOrderComment> commentPage = commentRepository.findAll(pageRequest);

        List<OmsOrderComment> commentList = commentPage.getContent();
        if (commentList == null || commentList.isEmpty()) {
            return com.futura.commerce.common.api.CommonResult.success(new PageImpl<>(Collections.emptyList(), pageRequest, 0), "No data available");
        }

        List<Long> commentIds = commentList.stream()
                .map(OmsOrderComment::getId)
                .filter(Objects::nonNull)
                .toList();

        List<ProductCommentImage> imageList = productCommentImageRepository.findByCommentIdIn(commentIds);
        List<CommentTagRelation> tagList = commentTagRelationRepository.findByCommentIdIn(commentIds);

        Map<Long, List<String>> imageMap = imageList.stream()
                .filter(img -> img.getCommentId() != null && img.getImgUrl() != null)
                .collect(Collectors.groupingBy(
                        ProductCommentImage::getCommentId,
                        Collectors.mapping(ProductCommentImage::getImgUrl, Collectors.toList())
                ));

        Map<Long, List<Long>> tagMap = tagList.stream()
                .filter(tag -> tag.getCommentId() != null && tag.getTagId() != null)
                .collect(Collectors.groupingBy(
                        CommentTagRelation::getCommentId,
                        Collectors.mapping(CommentTagRelation::getTagId, Collectors.toList())
                ));

        List<AdminCommentDTO> dtoList = commentList.stream().map(comment -> {
            AdminCommentDTO dto = new AdminCommentDTO();
            BeanUtils.copyProperties(comment, dto);
            dto.setCommentImage(imageMap.getOrDefault(comment.getId(), new ArrayList<>()));
            dto.setTagIds(tagMap.getOrDefault(comment.getId(), new ArrayList<>()));
            return dto;
        }).toList();

        Page<AdminCommentDTO> resultPage = new PageImpl<>(dtoList, pageRequest, commentPage.getTotalElements());
        return com.futura.commerce.common.api.CommonResult.success(resultPage, "Review list retrieved successfully");
    }
}
