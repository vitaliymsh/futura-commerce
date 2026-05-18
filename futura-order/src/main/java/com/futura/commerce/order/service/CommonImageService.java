package com.futura.commerce.order.service;

import com.futura.commerce.common.api.CommonResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * Common image upload service interface
 *
 * @author Vitalii
 */
public interface CommonImageService {

    CommonResult<String> upload(MultipartFile file);
}
