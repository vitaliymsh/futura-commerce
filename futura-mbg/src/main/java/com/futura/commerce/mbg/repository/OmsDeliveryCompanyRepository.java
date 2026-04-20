package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.OmsDeliveryCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for OmsDeliveryCompany
 *
 * @author Vitalii
 */
@Repository
public interface OmsDeliveryCompanyRepository extends JpaRepository<OmsDeliveryCompany, Long>, JpaSpecificationExecutor<OmsDeliveryCompany> {

    List<OmsDeliveryCompany> findByStatusOrderBySortAsc(Integer status);

    Optional<OmsDeliveryCompany> findByCompanyCode(String companyCode);
}
