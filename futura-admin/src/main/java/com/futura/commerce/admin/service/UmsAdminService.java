package com.futura.commerce.admin.service;

import com.futura.commerce.common.baseCommon.CommonResult;
import com.futura.commerce.mbg.model.UmsAdmin;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for administrative user operations
 *
 * @author Vitalii
 */
public interface UmsAdminService {

    List<UmsAdmin> findAll();

    Optional<UmsAdmin> findById(Long id);

    UmsAdmin save(UmsAdmin entity);

    void deleteById(Long id);

    CommonResult<String> login(String username, String password);

    CommonResult<Long> sum(Long id);

    CommonResult<String> uploadPicture(MultipartFile file);

    CommonResult<String> getPicture();
}
