package com.futura.commerce.admin.service;

import com.futura.commerce.admin.dto.OmsOrderSearchDTO;
import com.futura.commerce.admin.export.OmsOrderExcel;
import com.futura.commerce.admin.export.OmsOrderImportExcel;
import com.futura.commerce.admin.vo.OmsOrderListVO;
import com.futura.commerce.admin.vo.OmsOrderVO;
import com.futura.commerce.common.api.CommonResult;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Service interface for order operations
 *
 * @author Vitalii
 */
public interface OmsOrderService {

    CommonResult<OmsOrderVO> getOrderDetailById(Long id);

    CommonResult<Page<OmsOrderListVO>> getOrderList(Long pageNum, Long pageSize);

    CommonResult<Page<OmsOrderListVO>> search(OmsOrderSearchDTO dto);

    CommonResult<List<OmsOrderExcel>> exportExcel(OmsOrderSearchDTO dto);

    CommonResult<List<OmsOrderListVO>> excelImport(List<OmsOrderImportExcel> list);

    CommonResult<List<OmsOrderListVO>> getOrderByIds(List<Long> ids);
}
