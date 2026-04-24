package com.futura.commerce.product.service;

import com.futura.commerce.common.baseCommon.CommonResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * Common image upload service
 *
 * @author Vitalii
 */
public interface CommonImageService {

    CommonResult<String> upload(MultipartFile file);
}
