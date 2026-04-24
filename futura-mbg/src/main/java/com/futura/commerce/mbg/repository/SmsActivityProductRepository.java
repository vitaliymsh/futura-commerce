package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.SmsActivityProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for SmsActivityProduct
 *
 * @author Vitalii
 */
@Repository
public interface SmsActivityProductRepository extends JpaRepository<SmsActivityProduct, Long> {
    List<SmsActivityProduct> findByActivityId(Long activityId);
    List<SmsActivityProduct> findByProductId(Long productId);
    void deleteByActivityId(Long activityId);
}
