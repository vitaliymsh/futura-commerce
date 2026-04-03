package com.futura.commerce.admin.service;

import com.futura.commerce.mbg.model.UmsRoleMenu;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing UmsRoleMenu
 *
 * @author Vitalii
 */
public interface UmsRoleMenuService {

    List<UmsRoleMenu> findAll();

    Optional<UmsRoleMenu> findById(Long id);

    UmsRoleMenu save(UmsRoleMenu entity);

    void deleteById(Long id);
}
