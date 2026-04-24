package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.OmsOrderAfterSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for OmsOrderAfterSales
 *
 * @author Vitalii
 */
@Repository
public interface OmsOrderAfterSalesRepository extends JpaRepository<OmsOrderAfterSales, Long> {
    List<OmsOrderAfterSales> findByOrderId(Long orderId);
    List<OmsOrderAfterSales> findByUserId(Long userId);
}
