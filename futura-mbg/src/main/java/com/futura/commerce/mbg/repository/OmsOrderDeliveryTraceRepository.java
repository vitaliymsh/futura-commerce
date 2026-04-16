package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.OmsOrderDeliveryTrace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for OmsOrderDeliveryTrace
 *
 * @author Vitalii
 */
@Repository
public interface OmsOrderDeliveryTraceRepository extends JpaRepository<OmsOrderDeliveryTrace, Long>, JpaSpecificationExecutor<OmsOrderDeliveryTrace> {

    List<OmsOrderDeliveryTrace> findByDeliveryId(Long deliveryId);
}
