package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.MyCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for MyCoupon
 *
 * @author Vitalii
 */
@Repository
public interface MyCouponRepository extends JpaRepository<MyCoupon, Long> {
    List<MyCoupon> findByUserId(Long userId);
    List<MyCoupon> findByUserIdAndStatus(Long userId, Integer status);
    List<MyCoupon> findByUserIdAndCouponType(Long userId, Long couponType);
    long countByUserIdAndCouponType(Long userId, Long couponType);
}
