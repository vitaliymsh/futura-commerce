package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.UmsRoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Spring Data JPA repository for UmsRoleMenu
 *
 * @author Vitalii
 */
public interface UmsRoleMenuRepository extends JpaRepository<UmsRoleMenu, Long>, JpaSpecificationExecutor<UmsRoleMenu> {

    List<UmsRoleMenu> findByRoleId(Long roleId);
}
