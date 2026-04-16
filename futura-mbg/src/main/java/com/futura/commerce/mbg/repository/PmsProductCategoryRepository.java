package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.PmsProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for PmsProductCategory
 *
 * @author Vitalii
 */
public interface PmsProductCategoryRepository extends JpaRepository<PmsProductCategory, Long>, JpaSpecificationExecutor<PmsProductCategory> {
    long countByParentId(Long parentId);
}
