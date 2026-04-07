package com.futura.commerce.admin.controller;

import com.futura.commerce.admin.dto.IsPromotionVO;
import com.futura.commerce.admin.service.PmsProductService;
import com.futura.commerce.common.baseCommon.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Product promotion configuration controller
 *
 * @author Vitalii
 */
@Slf4j
@RestController
@RequestMapping("/Pms_promotion")
@Tag(name = "Pms_PromotionController", description = "Product promotion toggles and management")
public class Pms_PromotionController {

    @Resource
    private PmsProductService pmsProductService;

    /**
     * Enable or disable product promotion status
     */
    @PostMapping("/is_open")
    @Operation(summary = "Toggle product promotion status", description = "Switch promotion status on or off for a product")
    public CommonResult<String> isOpen(@RequestBody IsPromotionVO promotionVO) {
        return pmsProductService.isOpen(promotionVO);
    }
}
