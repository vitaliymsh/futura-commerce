package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.service.UmsRoleMenuService;
import com.futura.commerce.mbg.model.UmsRoleMenu;
import com.futura.commerce.mbg.repository.UmsRoleMenuRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing UmsRoleMenu
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class UmsRoleMenuServiceImpl implements UmsRoleMenuService {

    @Resource
    private UmsRoleMenuRepository umsRoleMenuRepository;

    @Override
    public List<UmsRoleMenu> findAll() {
        return umsRoleMenuRepository.findAll();
    }

    @Override
    public Optional<UmsRoleMenu> findById(Long id) {
        return umsRoleMenuRepository.findById(id);
    }

    @Override
    public UmsRoleMenu save(UmsRoleMenu entity) {
        return umsRoleMenuRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        umsRoleMenuRepository.deleteById(id);
    }
}
