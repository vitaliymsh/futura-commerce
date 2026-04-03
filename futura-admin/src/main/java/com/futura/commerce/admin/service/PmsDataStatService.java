package com.futura.commerce.admin.service;

import com.futura.commerce.mbg.model.PmsDataStat;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing PmsDataStat
 *
 * @author Vitalii
 */
public interface PmsDataStatService {

    List<PmsDataStat> findAll();

    Optional<PmsDataStat> findById(Long id);

    PmsDataStat save(PmsDataStat entity);

    void deleteById(Long id);
}
