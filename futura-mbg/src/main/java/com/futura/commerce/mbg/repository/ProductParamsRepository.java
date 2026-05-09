package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.ProductParams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for ProductParams
 *
 * @author Vitalii
 */
public interface ProductParamsRepository extends JpaRepository<ProductParams, Long>, JpaSpecificationExecutor<ProductParams> {
    List<ProductParams> findByProductsId(Long productsId);
}
