package com.futura.commerce.mbg.repository;

import com.futura.commerce.mbg.model.UmsMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data JPA repository for UmsMenu
 *
 * @author Vitalii
 */
public interface UmsMenuRepository extends JpaRepository<UmsMenu, Long>, JpaSpecificationExecutor<UmsMenu> {
}
