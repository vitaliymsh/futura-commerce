package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.service.SmsActivityService;
import com.futura.commerce.mbg.model.SmsActivity;
import com.futura.commerce.mbg.repository.SmsActivityRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing SmsActivity
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class SmsActivityServiceImpl implements SmsActivityService {

    @Resource
    private SmsActivityRepository smsActivityRepository;

    @Override
    public List<SmsActivity> findAll() {
        return smsActivityRepository.findAll();
    }

    @Override
    public Optional<SmsActivity> findById(Long id) {
        return smsActivityRepository.findById(id);
    }

    @Override
    public SmsActivity save(SmsActivity entity) {
        return smsActivityRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        smsActivityRepository.deleteById(id);
    }
}
