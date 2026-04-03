package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.OmsDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for OmsDelivery
 *
 * @author Vitalii
 */
public interface OmsDeliveryRepository extends JpaRepository<OmsDelivery, Long>, JpaSpecificationExecutor<OmsDelivery> {
}
