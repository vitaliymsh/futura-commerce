package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.OmsOrderDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for OmsOrderDelivery
 *
 * @author Vitalii
 */
@Repository
public interface OmsOrderDeliveryRepository extends JpaRepository<OmsOrderDelivery, Long>, JpaSpecificationExecutor<OmsOrderDelivery> {

    Optional<OmsOrderDelivery> findByOrderId(Long orderId);

    Optional<OmsOrderDelivery> findByOrderNo(String orderNo);
}
