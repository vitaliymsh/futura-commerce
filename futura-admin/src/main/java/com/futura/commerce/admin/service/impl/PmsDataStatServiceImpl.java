package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.service.PmsDataStatService;
import com.futura.commerce.mbg.model.PmsDataStat;
import com.futura.commerce.mbg.repository.PmsDataStatRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing PmsDataStat
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class PmsDataStatServiceImpl implements PmsDataStatService {

    @Resource
    private PmsDataStatRepository pmsDataStatRepository;

    @Override
    public List<PmsDataStat> findAll() {
        return pmsDataStatRepository.findAll();
    }

    @Override
    public Optional<PmsDataStat> findById(Long id) {
        return pmsDataStatRepository.findById(id);
    }

    @Override
    public PmsDataStat save(PmsDataStat entity) {
        return pmsDataStatRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        pmsDataStatRepository.deleteById(id);
    }
}
