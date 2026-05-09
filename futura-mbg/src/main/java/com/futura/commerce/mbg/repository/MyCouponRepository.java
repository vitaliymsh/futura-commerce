package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.MyCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    /**
     * Mark coupon as used
     *
     * @param myCouponId user coupon record ID
     * @return updated row count
     */
    @Modifying
    @Query("UPDATE MyCoupon c SET c.status = 1, c.useTime = CURRENT_TIMESTAMP WHERE c.id = :myCouponId AND c.status = 0")
    int useCoupon(@Param("myCouponId") Long myCouponId);

    /**
     * Cancel coupon usage
     *
     * @param myCouponId user coupon record ID
     * @return updated row count
     */
    @Modifying
    @Query("UPDATE MyCoupon c SET c.status = 0, c.useTime = NULL WHERE c.id = :myCouponId AND c.status = 1")
    int cancelCoupon(@Param("myCouponId") Long myCouponId);
}
