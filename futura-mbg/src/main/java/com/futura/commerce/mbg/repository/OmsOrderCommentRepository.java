package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.OmsOrderComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for OmsOrderComment
 *
 * @author Vitalii
 */
@Repository
public interface OmsOrderCommentRepository extends JpaRepository<OmsOrderComment, Long> {
    List<OmsOrderComment> findByOrderId(Long orderId);
    List<OmsOrderComment> findByProductId(Long productId);
    List<OmsOrderComment> findByProductIdOrderByCommentTimeDesc(Long productId);
    List<OmsOrderComment> findByUserId(Long userId);
}
