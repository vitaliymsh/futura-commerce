package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.SmsCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for SmsCoupon
 *
 * @author Vitalii
 */
@Repository
public interface SmsCouponRepository extends JpaRepository<SmsCoupon, Long>, JpaSpecificationExecutor<SmsCoupon> {

    List<SmsCoupon> findByActivityId(Long activityId);
}
