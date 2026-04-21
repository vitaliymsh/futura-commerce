package com.futura.commerce.admin.controller;

import com.futura.commerce.admin.service.SmsSeckillService;
import com.futura.commerce.admin.vo.SmsFlashSaleVO;
import com.futura.commerce.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Flash sale marketing activity controller
 *
 * @author Vitalii
 */
@Slf4j
@RestController
@RequestMapping("/activity")
@Tag(name = "SkillController", description = "Flash sale activity management")
public class SkillController {

    @Resource
    private SmsSeckillService smsSeckillService;

    @RequestMapping("/list")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','activity:view')")
    @Operation(summary = "Flash sale activity list", description = "Paginated list of flash sale activity products")
    public CommonResult<Page<SmsFlashSaleVO>> skillList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "1") Integer type) {
        return smsSeckillService.getSkillList(pageNum, pageSize, type);
    }
}
