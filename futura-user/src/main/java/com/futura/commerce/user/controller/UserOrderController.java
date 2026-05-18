package com.futura.commerce.user.controller;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.feign.order.OrderFeignClient;
import com.futura.commerce.feign.product.ProductFeignClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * Customer order and interaction proxy controller
 *
 * @author Vitalii
 */
@RestController
@RequestMapping("/user")
@Tag(name = "UserOrderController", description = "Customer order reviews and interaction events")
public class UserOrderController {

    @Resource
    private OrderFeignClient orderFeignClient;

    @Resource
    private ProductFeignClient productFeignClient;

    @GetMapping("/comment/{productId}")
    @Operation(summary = "Get product comments", description = "Get customer reviews for a given product")
    public CommonResult<?> userComment(@PathVariable Long productId) {
        return orderFeignClient.orderComment(productId);
    }

    @PostMapping("/comment/create")
    @Operation(summary = "Create product comment", description = "Submit a customer product review and rating")
    public CommonResult<?> createComment(@RequestBody Object dto) {
        return orderFeignClient.saveComment(dto);
    }

    @PostMapping(value = "/upload/image", consumes = "multipart/form-data")
    @Operation(summary = "Upload review image", description = "Upload photo attachment for product comment")
    public CommonResult<?> uploadImage(@RequestPart("file") org.springframework.web.multipart.MultipartFile file) {
        return orderFeignClient.uploadCommentImage(file);
    }

    @PostMapping("/category/click/report")
    @Operation(summary = "Category click tracking report", description = "Forward category click analytics")
    public CommonResult<?> userCategoryClickReport(@RequestBody Object dto) {
        return productFeignClient.productClickReport(dto);
    }

    @GetMapping("/order/list")
    @Operation(summary = "Get user order list", description = "Query customer orders")
    public CommonResult<?> listOrders(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return orderFeignClient.orderList(page, pageSize);
    }

    @PostMapping("/order/seckill")
    @Operation(summary = "Seckill order submission", description = "Place rush order asynchronously")
    public CommonResult<?> seckillOrder(@RequestBody(required = false) Object dto) {
        return CommonResult.success("Seckill order queued successfully");
    }
}
