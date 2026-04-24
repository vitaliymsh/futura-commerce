package com.futura.commerce.product.service.impl;

import com.futura.commerce.product.service.CommonImageService;
import com.futura.commerce.common.baseCommon.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/**
 * Common image upload service implementation
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class CommonImageServiceImpl implements CommonImageService {

    private static final String IMAGE_ACCESS_PATH = "/pic/";

    @Override
    public CommonResult<String> upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return CommonResult.failed("Upload failed: no file provided");
        }
        try {
            String filename = file.getOriginalFilename();
            String suffix = ".jpg";
            if (filename != null && filename.contains(".")) {
                suffix = filename.substring(filename.lastIndexOf("."));
            }
            String newFileName = UUID.randomUUID() + suffix;

            String uploadDirStr = System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "pic" + File.separator;
            File uploadDir = new File(uploadDirStr);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            File destFile = new File(uploadDir, newFileName);
            file.transferTo(destFile);

            return CommonResult.success(IMAGE_ACCESS_PATH + newFileName, "Image uploaded successfully");
        } catch (Exception e) {
            log.error("Image upload failed", e);
            return CommonResult.failed("Upload failed: " + e.getMessage());
        }
    }
}
