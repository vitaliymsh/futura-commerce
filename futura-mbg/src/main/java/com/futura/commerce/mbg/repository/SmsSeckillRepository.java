package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.SmsSeckill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for SmsSeckill
 *
 * @author Vitalii
 */
@Repository
public interface SmsSeckillRepository extends JpaRepository<SmsSeckill, Long>, JpaSpecificationExecutor<SmsSeckill> {

    List<SmsSeckill> findByActivityId(Long activityId);

    /**
     * Optimistic locking inventory decrement for seckill SKU
     *
     * @param skuId SKU identifier
     * @param buyNum purchase quantity
     * @return number of affected rows (>0 success, <=0 insufficient stock)
     */
    @Modifying
    @Query("UPDATE SmsSeckill s SET s.stock = s.stock - :buyNum, s.soldStock = s.soldStock + :buyNum WHERE s.skuId = :skuId AND s.stock >= :buyNum")
    int decrStock(@Param("skuId") Long skuId, @Param("buyNum") Integer buyNum);
}
