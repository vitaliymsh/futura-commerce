package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.PmsDataStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for PmsDataStat
 *
 * @author Vitalii
 */
public interface PmsDataStatRepository extends JpaRepository<PmsDataStat, Long>, JpaSpecificationExecutor<PmsDataStat> {

    List<PmsDataStat> findByStatDate(LocalDate statDate);

    List<PmsDataStat> findByStatDateBetweenOrderByStatDateAsc(LocalDate startDate, LocalDate endDate);
}
