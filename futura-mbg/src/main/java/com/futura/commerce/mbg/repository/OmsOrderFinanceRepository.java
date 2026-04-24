package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.OmsOrderFinance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for OmsOrderFinance
 *
 * @author Vitalii
 */
@Repository
public interface OmsOrderFinanceRepository extends JpaRepository<OmsOrderFinance, Long> {
    Optional<OmsOrderFinance> findByOrderId(Long orderId);
    Optional<OmsOrderFinance> findByOrderNo(String orderNo);
}
