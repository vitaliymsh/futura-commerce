package com.futura.commerce.admin.service;

import com.futura.commerce.mbg.model.UmsMenu;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing UmsMenu
 *
 * @author Vitalii
 */
public interface UmsMenuService {

    List<UmsMenu> findAll();

    Optional<UmsMenu> findById(Long id);

    UmsMenu save(UmsMenu entity);

    void deleteById(Long id);
}
