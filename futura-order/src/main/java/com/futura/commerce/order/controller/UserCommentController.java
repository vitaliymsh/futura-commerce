package com.futura.commerce.order.controller;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.order.dto.OrderCommentDTO;
import com.futura.commerce.order.service.OmsOrderCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Customer product comments controller
 *
 * @author Vitalii
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "UserCommentController", description = "Customer product reviews and comments")
public class UserCommentController {

    @Resource
    private OmsOrderCommentService omsOrderCommentService;

    @GetMapping("/comment/{productId}")
    @Operation(summary = "Get product comments", description = "Retrieve list of customer reviews for a given product")
    public CommonResult<List<OrderCommentDTO>> orderComment(@PathVariable Long productId) {
        return omsOrderCommentService.orderComment(productId);
    }
}
