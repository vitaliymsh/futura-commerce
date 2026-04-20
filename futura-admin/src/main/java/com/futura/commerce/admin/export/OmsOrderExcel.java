package com.futura.commerce.admin.export;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Order export model for EasyExcel
 *
 * @author Vitalii
 */
@Data
public class OmsOrderExcel {

    @ExcelProperty(value = "Order ID", index = 0)
    private Long id;

    @ExcelProperty(value = "Order No", index = 1)
    private String orderNo;

    @ExcelProperty(value = "Buyer Info", index = 2)
    private String buyerInfo;

    @ExcelProperty(value = "Pay Time", index = 3)
    private LocalDateTime payTime;

    @ExcelProperty(value = "Order Amount", index = 4)
    private BigDecimal totalAmount;

    @ExcelProperty(value = "Paid Amount", index = 5)
    private BigDecimal payAmount;

    @ExcelProperty(value = "Order Status", index = 6)
    private Integer status;

    @ExcelProperty(value = "Delivery Status", index = 7)
    private Integer deliveryStatus;

    @ExcelProperty(value = "Delivery Time", index = 8)
    private LocalDateTime deliveryTime;
}
