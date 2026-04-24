package com.futura.commerce.order.controller;

import com.alibaba.excel.EasyExcel;
import com.futura.commerce.order.dto.BatchExportDTO;
import com.futura.commerce.order.dto.OmsOrderSearchDTO;
import com.futura.commerce.order.export.OmsOrderExcel;
import com.futura.commerce.order.export.OmsOrderImportExcel;
import com.futura.commerce.order.service.OmsOrderService;
import com.futura.commerce.order.vo.OmsOrderListVO;
import com.futura.commerce.order.vo.OmsOrderVO;
import com.futura.commerce.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

/**
 * Order details and operations controller
 *
 * @author Vitalii
 */
@Slf4j
@RestController
@RequestMapping("/order")
@Tag(name = "OmsOrderController", description = "Order management")
public class OmsOrderController {

    @Resource
    private OmsOrderService omsOrderService;

    /**
     * Get order details
     */
    @GetMapping("/detail/{id}")
    @Operation(summary = "Get order details", description = "Retrieve complete order details including delivery and items")
    public CommonResult<OmsOrderVO> orderDetail(@PathVariable Long id) {
        return omsOrderService.getOrderDetailById(id);
    }

    /**
     * Get order list
     */
    @GetMapping("/list")
    @Operation(summary = "Get order list", description = "Paginated list of orders with delivery status")
    public CommonResult<Page<OmsOrderListVO>> orderList(
            @RequestParam(value = "pageNum", required = false) Long pageNum,
            @RequestParam(value = "page", required = false) Long page,
            @RequestParam(defaultValue = "10") Long pageSize) {
        long p = pageNum != null ? pageNum : (page != null ? page : 1L);
        return omsOrderService.getOrderList(p, pageSize);
    }

    /**
     * Search orders
     */
    @PostMapping("/search")
    @Operation(summary = "Search orders", description = "Filter orders with multi-criteria conditions")
    public CommonResult<Page<OmsOrderListVO>> search(@RequestBody OmsOrderSearchDTO dto) {
        return omsOrderService.search(dto);
    }

    /**
     * Export orders to Excel
     */
    @PostMapping("/export")
    @Operation(summary = "Export orders to Excel", description = "Stream Excel sheet containing filtered order data")
    public void exportExcel(@RequestBody OmsOrderSearchDTO dto, HttpServletResponse response) throws IOException {
        CommonResult<List<OmsOrderExcel>> result = omsOrderService.exportExcel(dto);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("order_list_" + LocalDate.now(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), OmsOrderExcel.class)
                .sheet("Orders")
                .doWrite(result.getData());
    }

    /**
     * Import orders from Excel
     */
    @PostMapping("/import")
    @Operation(summary = "Import orders from Excel", description = "Bulk create orders from uploaded Excel spreadsheet")
    public CommonResult<List<OmsOrderListVO>> excelImport(@RequestParam("file") MultipartFile file) throws IOException {
        List<OmsOrderImportExcel> list = EasyExcel.read(file.getInputStream())
                .head(OmsOrderImportExcel.class)
                .sheet()
                .doReadSync();
        return omsOrderService.excelImport(list);
    }

    /**
     * Batch export selected orders
     */
    @PostMapping("/batch-export")
    @Operation(summary = "Batch export selected orders", description = "Export specifically selected orders by ID list")
    public void batchExportOrder(@RequestBody BatchExportDTO dto, HttpServletResponse response) throws Exception {
        List<Long> ids = dto.getIds();
        if (ids == null || ids.isEmpty()) {
            return;
        }

        CommonResult<List<OmsOrderListVO>> result = omsOrderService.getOrderByIds(ids);
        List<OmsOrderListVO> voList = result.getData();

        List<OmsOrderExcel> excelList = voList.stream().map(vo -> {
            OmsOrderExcel excel = new OmsOrderExcel();
            BeanUtils.copyProperties(vo, excel);
            return excel;
        }).toList();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("batch_orders_" + LocalDate.now(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");

        EasyExcel.write(response.getOutputStream(), OmsOrderExcel.class)
                .sheet("Orders")
                .doWrite(excelList);
    }
}
