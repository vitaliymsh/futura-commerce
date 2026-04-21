package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.SmsSeckill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
}
