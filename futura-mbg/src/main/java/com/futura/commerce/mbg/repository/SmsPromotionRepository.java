package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.SmsPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for SmsPromotion
 *
 * @author Vitalii
 */
public interface SmsPromotionRepository extends JpaRepository<SmsPromotion, Long>, JpaSpecificationExecutor<SmsPromotion> {

    List<SmsPromotion> findByIsCategoryOrderByCreateTimeDesc(Integer isCategory);

    List<SmsPromotion> findByProductId(Long productId);
}
