package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.PmsProductSku;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for PmsProductSku entity
 *
 * @author Vitalii
 */
@Repository
public interface PmsProductSkuRepository extends JpaRepository<PmsProductSku, Long>, JpaSpecificationExecutor<PmsProductSku> {

    List<PmsProductSku> findByProductIdAndDeletedSku(Long productId, Integer deletedSku);

    List<PmsProductSku> findByProductId(Long productId);

    List<PmsProductSku> findByIdIn(java.util.Collection<Long> ids);

    Page<PmsProductSku> findByDeletedSku(Integer deletedSku, Pageable pageable);

    void deleteByProductId(Long productId);
}
