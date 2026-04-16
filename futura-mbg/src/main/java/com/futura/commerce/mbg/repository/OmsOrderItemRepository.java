package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.OmsOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for OmsOrderItem
 *
 * @author Vitalii
 */
@Repository
public interface OmsOrderItemRepository extends JpaRepository<OmsOrderItem, Long>, JpaSpecificationExecutor<OmsOrderItem> {

    List<OmsOrderItem> findByOrderId(Long orderId);
}
