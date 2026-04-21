package com.futura.commerce.admin.controller;

import com.futura.commerce.admin.dto.ActivitySearchDTO;
import com.futura.commerce.admin.dto.SmsSeckillUpdateDTO;
import com.futura.commerce.admin.service.SmsSeckillService;
import com.futura.commerce.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for flash sale promotion management
 *
 * @author Vitalii
 */
@RestController
@RequestMapping("/skill")
@Tag(name = "SkillController", description = "Flash sale activity management")
public class SkillController {

    @Resource
    private SmsSeckillService smsSeckillService;

    @PostMapping("/update/{id}")
    @Operation(summary = "Edit flash sale activity", description = "Update flash sale activity and its basic information")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','activity:view')")
    public CommonResult<SmsSeckillUpdateDTO> skillEdit(@PathVariable Long id, @RequestBody SmsSeckillUpdateDTO smsSeckill) {
        return smsSeckillService.skillEdit(id, smsSeckill);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete flash sale activity", description = "Delete a single flash sale activity")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','activity:view')")
    public CommonResult<String> skillDelete(@PathVariable Long id) {
        return smsSeckillService.skillDelete(id);
    }

    @DeleteMapping("/delete/batch")
    @Operation(summary = "Batch delete flash sale activities", description = "Batch delete multiple flash sale activities")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','activity:view')")
    public CommonResult<String> skillDeleteBatch(@RequestBody Long[] ids) {
        return smsSeckillService.skillDeleteBatch(ids);
    }

    @PostMapping("/search")
    @Operation(summary = "Search flash sale activities", description = "Search flash sale activities by filter parameters")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('user:manage','activity:view')")
    public CommonResult<ActivitySearchDTO> skillSearch(@RequestBody ActivitySearchDTO smsSeckill) {
        return smsSeckillService.skillSearch(smsSeckill);
    }
}
