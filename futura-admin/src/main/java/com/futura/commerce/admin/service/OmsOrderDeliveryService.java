package com.futura.commerce.admin.service;

import com.futura.commerce.admin.dto.DeliveryShipDTO;
import com.futura.commerce.admin.dto.OmsOrderDeliveryCancelDTO;
import com.futura.commerce.admin.dto.OmsOrderDeliverySearchDTO;
import com.futura.commerce.admin.dto.UpdateTrackingNoDTO;
import com.futura.commerce.admin.vo.OmsDeliveryAndTraceVO;
import com.futura.commerce.admin.vo.OmsDeliveryVO;
import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.OmsDeliveryCompany;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * Service interface for order delivery management
 *
 * @author Vitalii
 */
public interface OmsOrderDeliveryService {

    CommonResult<List<OmsDeliveryVO>> selcetOrderAndDeliveryList();

    CommonResult<?> getLogisticsById(Long id);

    CommonResult<?> search(String orderNo, Integer deliveryStatus);

    CommonResult<Page<OmsDeliveryAndTraceVO>> selectOrderAndDeliveryList(Integer page, Integer pageSize, Integer[] deliveryStatus);

    CommonResult<Page<OmsDeliveryAndTraceVO>> search(OmsOrderDeliverySearchDTO dto);

    CommonResult<List<OmsDeliveryCompany>> getDeliveryCompanyList();

    CommonResult<Map<String, Long>> selectStatus();

    CommonResult<DeliveryShipDTO> ship(Long orderId, DeliveryShipDTO dto);

    CommonResult<?> updateTrackingNo(Long orderId, UpdateTrackingNoDTO dto);

    CommonResult<?> cancelDelivery(Long orderId, OmsOrderDeliveryCancelDTO dto);
}
