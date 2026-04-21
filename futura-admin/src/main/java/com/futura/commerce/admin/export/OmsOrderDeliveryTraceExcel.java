package com.futura.commerce.admin.export;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Delivery trace export model for EasyExcel
 *
 * @author Vitalii
 */
@Data
public class OmsOrderDeliveryTraceExcel {

    @ExcelProperty(value = "Order No", index = 0)
    private String orderNo;

    @ExcelProperty(value = "Tracking No", index = 1)
    private String deliveryNo;

    @ExcelProperty(value = "Receiver Name", index = 2)
    private String receiverName;

    @ExcelProperty(value = "Receiver Phone", index = 3)
    private String receiverPhone;

    @ExcelProperty(value = "Carrier ID", index = 4)
    private Integer deliveryCompany;

    @ExcelProperty(value = "Delivery Start Time", index = 5)
    private String deliveryStartTime;

    @ExcelProperty(value = "Delivery End Time", index = 6)
    private String deliveryEndTime;

    @ExcelProperty(value = "Trace Start Time", index = 7)
    private String traceStartTime;

    @ExcelProperty(value = "Trace End Time", index = 8)
    private String traceEndTime;

    @ExcelProperty(value = "Trace Status", index = 9)
    private Integer traceStatus;
}
