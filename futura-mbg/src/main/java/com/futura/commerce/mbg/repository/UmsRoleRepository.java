package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.UmsRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for UmsRole
 *
 * @author Vitalii
 */
@Repository
public interface UmsRoleRepository extends JpaRepository<UmsRole, Long> {
}
