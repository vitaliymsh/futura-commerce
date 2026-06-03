package com.futura.commerce.order.dto;

import com.futura.commerce.mbg.model.OmsOrderAfterSales;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * After-sales order item details DTO
 *
 * @author Vitalii
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AfterOrderDTO extends OmsOrderAfterSales {
    private String pic;
    private String spec;
    private String model;
}
