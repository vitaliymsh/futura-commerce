package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.SmsFullReduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for SmsFullReduction
 *
 * @author Vitalii
 */
@Repository
public interface SmsFullReductionRepository extends JpaRepository<SmsFullReduction, Long>, JpaSpecificationExecutor<SmsFullReduction> {

    List<SmsFullReduction> findByActivityId(Long activityId);
}
