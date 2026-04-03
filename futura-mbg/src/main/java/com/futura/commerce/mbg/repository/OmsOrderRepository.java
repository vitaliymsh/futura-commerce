package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.OmsOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for OmsOrder
 *
 * @author Vitalii
 */
public interface OmsOrderRepository extends JpaRepository<OmsOrder, Long>, JpaSpecificationExecutor<OmsOrder> {

    List<OmsOrder> findByUserId(Long userId);

    List<OmsOrder> findByUserIdOrderByCreateTimeDesc(Long userId);
}
