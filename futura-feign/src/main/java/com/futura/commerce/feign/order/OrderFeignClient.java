package com.futura.commerce.feign.order;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.common.dto.AiOrderProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Feign client interface for Futura Order service
 *
 * @author Vitalii
 */
@FeignClient(name = "futura-order", url = "${futura.order.url:http://localhost:8083}", contextId = "orderClient")
public interface OrderFeignClient {

    @GetMapping("/item/getByOrderNo")
    AiOrderProductDto getByOrderNo(@RequestParam("orderNo") String orderNo);

    @GetMapping("/user/comment/{productId}")
    CommonResult<?> orderComment(@PathVariable("productId") Long productId);

    @PostMapping("/user/comment/create")
    CommonResult<?> saveComment(@RequestBody Object orderCommentDTO);

    @PostMapping(value = "/user/upload/image", consumes = "multipart/form-data")
    CommonResult<String> uploadCommentImage(@RequestPart("file") MultipartFile file);

    @GetMapping("/review/list")
    CommonResult<?> reviewList(@RequestParam("page") Integer page,
                               @RequestParam("pageSize") Integer pageSize);

    @GetMapping("/after/list")
    CommonResult<?> orderAfterList(@RequestParam("page") Integer page,
                                   @RequestParam("pageSize") Integer pageSize);

    @GetMapping("/order/detail/{id}")
    CommonResult<?> orderDetail(@PathVariable Long id);

    @GetMapping("/order/list")
    CommonResult<?> orderList(@RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer pageSize);

    @PostMapping("/order/search")
    CommonResult<?> search(@RequestBody Object dto);

    @PostMapping("/order/export")
    void orderExport(@RequestBody Object dto);

    @PostMapping("/order/import")
    CommonResult<?> orderImport(@RequestParam("file") MultipartFile file);

    @PostMapping("/order/batch-export")
    void batchExportOrder(@RequestBody Object dto);

    // Delivery endpoints
    @GetMapping("/delivery/list")
    CommonResult<?> deliveryList(@RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                 @RequestParam(required = false) Long orderId,
                                 @RequestParam(required = false) String receiverPhone,
                                 @RequestParam(required = false) Integer status);

    @GetMapping("/delivery/company/list")
    CommonResult<?> getDeliveryCompanyList();

    @GetMapping("/delivery/status")
    CommonResult<?> selectStatus();

    @PostMapping("/delivery/ship/{id}")
    CommonResult<?> ship(@PathVariable(value = "id") Long orderId, @RequestBody Object dto);

    @PutMapping("/delivery/tracking/{id}")
    CommonResult<?> updateTrackingNo(@PathVariable(value = "id") Long orderId, @RequestBody Object dto);

    @PostMapping("/delivery/search")
    CommonResult<?> searchDelivery(@RequestBody Object dto);

    @PostMapping("/delivery/cancel/{id}")
    CommonResult<?> cancelDelivery(@PathVariable(value = "id") Long orderId, @RequestBody Object dto);

    // Delivery trace endpoints
    @GetMapping("/deliveryTrace/list")
    CommonResult<?> traceList(@RequestParam Integer pageNum, @RequestParam Integer pageSize);

    @GetMapping("/deliveryTrace/logistics/{id}")
    CommonResult<?> getLogistics(@PathVariable Long id);

    @PostMapping("/deliveryTrace/update")
    CommonResult<?> updateDeliveryTrace(@RequestBody Object dto);

    @PostMapping("/deliveryTrace/search")
    CommonResult<?> searchDeliveryTrace(@RequestBody Object dto);

    @PostMapping("/deliveryTrace/saveColumnConfig")
    CommonResult<?> setDeliveryTraceColumn(@RequestBody List<Map<String, Object>> list);

    @PostMapping("/deliveryTrace/export")
    void exportDeliveryTrace(@RequestBody Object dto);

    // Order items
    @GetMapping("/item/list")
    CommonResult<?> itemList();
}
