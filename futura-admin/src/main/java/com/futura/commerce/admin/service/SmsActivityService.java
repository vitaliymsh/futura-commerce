package com.futura.commerce.admin.service;

import com.futura.commerce.mbg.model.SmsActivity;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing SmsActivity
 *
 * @author Vitalii
 */
public interface SmsActivityService {

    List<SmsActivity> findAll();

    Optional<SmsActivity> findById(Long id);

    SmsActivity save(SmsActivity entity);

    void deleteById(Long id);
}
