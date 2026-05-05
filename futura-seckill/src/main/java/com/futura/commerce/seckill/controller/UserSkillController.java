package com.futura.commerce.seckill.controller;

import com.futura.commerce.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Customer flash sale query controller
 *
 * @author Vitalii
 */
@RestController
@RequestMapping("/skill")
@Tag(name = "UserSkillController", description = "Customer flash sale activity queries")
public class UserSkillController {

    @RequestMapping("/list")
    @Operation(summary = "Flash sale list for customers", description = "Get available flash sale events for customers")
    public CommonResult<?> list() {
        return CommonResult.success(null, "Customer flash sale list query");
    }
}
