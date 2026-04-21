package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.SmsFollowDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for SmsFollowDiscount
 *
 * @author Vitalii
 */
@Repository
public interface SmsFollowDiscountRepository extends JpaRepository<SmsFollowDiscount, Long>, JpaSpecificationExecutor<SmsFollowDiscount> {

    List<SmsFollowDiscount> findByActivityId(Long activityId);
}
