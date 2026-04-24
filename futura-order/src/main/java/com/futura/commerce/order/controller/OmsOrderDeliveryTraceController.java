package com.futura.commerce.order.controller;

import com.alibaba.excel.EasyExcel;
import com.futura.commerce.order.dto.OmsOrderDeliveryTraceSearchDTO;
import com.futura.commerce.order.dto.OmsOrderDeliveryUpdateDTO;
import com.futura.commerce.order.export.OmsOrderDeliveryTraceExcel;
import com.futura.commerce.order.service.OmsOrderDeliveryTraceService;
import com.futura.commerce.order.vo.OmsDeliveryAndTraceVO;
import com.futura.commerce.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Order delivery trace tracking controller
 *
 * @author Vitalii
 */
@Slf4j
@RestController
@RequestMapping("/deliveryTrace")
@Tag(name = "DeliveryTraceController", description = "Order delivery trace management")
public class OmsOrderDeliveryTraceController {

    @Resource
    private OmsOrderDeliveryTraceService omsOrderDeliveryTraceService;

    /**
     * Get logistics delivery trace list with pagination and column preferences
     */
    @Operation(summary = "Get delivery trace list", description = "Retrieve paginated list of deliveries and column preferences")
    @GetMapping("/list")
    public CommonResult<Page<OmsDeliveryAndTraceVO>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return omsOrderDeliveryTraceService.getDeliveryList(pageNum, pageSize);
    }

    /**
     * Get logistics trace details
     */
    @GetMapping("/logistics/{id}")
    @Operation(summary = "Get logistics trace", description = "Get logistics and trace checkpoints by delivery ID")
    public CommonResult<Map<String, Object>> getLogistics(@PathVariable Long id) {
        return omsOrderDeliveryTraceService.getLogisticsById(id);
    }

    /**
     * Update order delivery logistics information
     */
    @Operation(summary = "Update order delivery info", description = "Update delivery courier, number, and receiver details")
    @PostMapping("/update")
    public CommonResult<?> update(@RequestBody OmsOrderDeliveryUpdateDTO dto) {
        return omsOrderDeliveryTraceService.updateDeliveryTraceInfo(dto);
    }

    /**
     * Search delivery trace information
     */
    @PostMapping("/search")
    @Operation(summary = "Search logistics info", description = "Search logistics information by criteria")
    public CommonResult<List<OmsDeliveryAndTraceVO>> search(@RequestBody OmsOrderDeliveryTraceSearchDTO dto) {
        return omsOrderDeliveryTraceService.searchDto(dto);
    }

    /**
     * Save table column display configuration
     */
    @PostMapping("/saveColumnConfig")
    @Operation(summary = "Save column configuration", description = "Save user personalized table column display and sort preferences")
    public CommonResult<?> setDeliveryTraceColumn(@RequestBody List<Map<String, Object>> list) {
        return omsOrderDeliveryTraceService.setDeliveryTraceColumn(list);
    }

    /**
     * Export delivery trace records to Excel
     */
    @PostMapping("/export")
    @Operation(summary = "Export delivery trace records", description = "Export delivery trace information into Excel file")
    public void exportExcel(@RequestBody OmsOrderDeliveryTraceSearchDTO dto, HttpServletResponse response) throws IOException {
        CommonResult<List<OmsOrderDeliveryTraceExcel>> result = omsOrderDeliveryTraceService.exportExcel(dto);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("delivery_trace_" + LocalDate.now(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), OmsOrderDeliveryTraceExcel.class)
                .sheet("DeliveryTrace")
                .doWrite(result.getData());
    }
}
