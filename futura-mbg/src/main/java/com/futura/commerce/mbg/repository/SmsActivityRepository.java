package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.SmsActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for SmsActivity
 *
 * @author Vitalii
 */
public interface SmsActivityRepository extends JpaRepository<SmsActivity, Long>, JpaSpecificationExecutor<SmsActivity> {
}
