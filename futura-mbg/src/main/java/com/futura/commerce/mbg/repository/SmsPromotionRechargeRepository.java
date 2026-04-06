package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.SmsPromotionRecharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for SmsPromotionRecharge
 *
 * @author Vitalii
 */
public interface SmsPromotionRechargeRepository extends JpaRepository<SmsPromotionRecharge, Long>, JpaSpecificationExecutor<SmsPromotionRecharge> {

    List<SmsPromotionRecharge> findByAdminIdOrderByRechargeTimeDesc(Long adminId);
}
