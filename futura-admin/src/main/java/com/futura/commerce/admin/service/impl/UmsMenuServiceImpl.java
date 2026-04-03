package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.service.UmsMenuService;
import com.futura.commerce.mbg.model.UmsMenu;
import com.futura.commerce.mbg.repository.UmsMenuRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing UmsMenu
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class UmsMenuServiceImpl implements UmsMenuService {

    @Resource
    private UmsMenuRepository umsMenuRepository;

    @Override
    public List<UmsMenu> findAll() {
        return umsMenuRepository.findAll();
    }

    @Override
    public Optional<UmsMenu> findById(Long id) {
        return umsMenuRepository.findById(id);
    }

    @Override
    public UmsMenu save(UmsMenu entity) {
        return umsMenuRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        umsMenuRepository.deleteById(id);
    }
}
