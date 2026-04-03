package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.UmsUserPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for UmsUserPromotion
 *
 * @author Vitalii
 */
public interface UmsUserPromotionRepository extends JpaRepository<UmsUserPromotion, Long>, JpaSpecificationExecutor<UmsUserPromotion> {
}
