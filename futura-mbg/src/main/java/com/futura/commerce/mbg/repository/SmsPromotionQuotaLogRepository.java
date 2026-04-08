package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.SmsPromotionQuotaLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for SmsPromotionQuotaLog
 *
 * @author Vitalii
 */
@Repository
public interface SmsPromotionQuotaLogRepository extends JpaRepository<SmsPromotionQuotaLog, Long> {

    List<SmsPromotionQuotaLog> findByProductIdAndStatus(Long productId, Integer status);
}
