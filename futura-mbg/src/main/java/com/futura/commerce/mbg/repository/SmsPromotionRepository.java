package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.SmsPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for SmsPromotion
 *
 * @author Vitalii
 */
public interface SmsPromotionRepository extends JpaRepository<SmsPromotion, Long>, JpaSpecificationExecutor<SmsPromotion> {

    List<SmsPromotion> findByIsCategoryOrderByCreateTimeDesc(Integer isCategory);

    List<SmsPromotion> findByProductId(Long productId);

    Optional<SmsPromotion> findFirstByProductIdAndStatus(Long productId, Integer status);

    void deleteByProductId(Long productId);
}
