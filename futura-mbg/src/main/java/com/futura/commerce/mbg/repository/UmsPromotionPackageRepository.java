package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.UmsPromotionPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for UmsPromotionPackage
 *
 * @author Vitalii
 */
public interface UmsPromotionPackageRepository extends JpaRepository<UmsPromotionPackage, Long>, JpaSpecificationExecutor<UmsPromotionPackage> {
}
