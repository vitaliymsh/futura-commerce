package com.futura.commerce.order.controller;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.order.dto.AdminCommentDTO;
import com.futura.commerce.order.service.OmsOrderCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Administrative order comment reviews controller
 *
 * @author Vitalii
 */
@Slf4j
@RestController
@RequestMapping("/review")
@Tag(name = "OmsCommentController", description = "Administrative order comment reviews")
public class OmsCommentController {

    @Resource
    private OmsOrderCommentService omsOrderCommentService;

    @GetMapping("/list")
    @Operation(summary = "Order review list", description = "Query paginated customer product reviews and comments")
    public CommonResult<Page<AdminCommentDTO>> reviewList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return omsOrderCommentService.adminCommentList(page, pageSize);
    }
}
