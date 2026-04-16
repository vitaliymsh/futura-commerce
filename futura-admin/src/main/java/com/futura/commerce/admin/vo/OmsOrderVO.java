package com.futura.commerce.admin.vo;

import com.futura.commerce.mbg.model.OmsOrder;
import com.futura.commerce.mbg.model.OmsOrderDelivery;
import com.futura.commerce.mbg.model.OmsOrderItem;
import lombok.Data;

import java.util.List;

/**
 * Order detail presentation VO
 *
 * @author Vitalii
 */
@Data
public class OmsOrderVO {
    private OmsOrder order;
    private OmsOrderDelivery delivery;
    private List<OmsOrderItem> orderItemList;
}
