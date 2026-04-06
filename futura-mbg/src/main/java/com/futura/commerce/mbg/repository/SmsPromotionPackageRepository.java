package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.SmsPromotionPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for SmsPromotionPackage
 *
 * @author Vitalii
 */
public interface SmsPromotionPackageRepository extends JpaRepository<SmsPromotionPackage, Long>, JpaSpecificationExecutor<SmsPromotionPackage> {
}
