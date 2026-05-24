package com.futura.commerce.product.service.impl;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.product.service.CommonImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Common image upload service implementation with path traversal prevention and extension validation
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class CommonImageServiceImpl implements CommonImageService {

    @Value("${futura.image.upload-dir:./uploads/pic/}")
    private String imageDir;

    @Value("${futura.image.access-path:/pic/}")
    private String imageAccessPath;

    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList(
            ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"
    ));
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    @Override
    public CommonResult<String> upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return CommonResult.failed("Upload failed: no file provided");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            return CommonResult.failed("File size exceeds 10MB limit");
        }

        try {
            String filename = file.getOriginalFilename();
            String suffix = ".jpg";
            if (filename != null && !filename.isEmpty()) {
                int dotIndex = filename.lastIndexOf(".");
                if (dotIndex >= 0) {
                    suffix = filename.substring(dotIndex).toLowerCase();
                }
            }

            if (!ALLOWED_EXTENSIONS.contains(suffix)) {
                return CommonResult.failed("Unsupported file extension: " + suffix);
            }

            String newFileName = UUID.randomUUID() + suffix;
            File uploadDir = new File(imageDir);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            File destFile = new File(uploadDir, newFileName);
            String canonicalDest = destFile.getCanonicalPath();
            String canonicalDir = uploadDir.getCanonicalPath();
            if (!canonicalDest.startsWith(canonicalDir)) {
                return CommonResult.failed("Invalid file path traversal detected");
            }

            file.transferTo(destFile);
            return CommonResult.success(imageAccessPath + newFileName, "Image uploaded successfully");
        } catch (Exception e) {
            log.error("Image upload failed", e);
            return CommonResult.failed("Upload failed: " + e.getMessage());
        }
    }
}
