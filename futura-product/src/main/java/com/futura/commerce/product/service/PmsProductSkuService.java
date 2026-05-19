package com.futura.commerce.product.service;

import com.futura.commerce.product.dto.PmsSkuSearchDTO;
import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.PmsProductSku;
import com.futura.commerce.mbg.model.PmsSkuPriceHistory;
import com.futura.commerce.mbg.model.SysOperationLog;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service interface for product SKU management
 *
 * @author Vitalii
 */
public interface PmsProductSkuService {

    CommonResult<String> updateSku(Long id, List<PmsProductSku> pmsProductSkuList);

    CommonResult<List<PmsProductSku>> selectSku();

    CommonResult<String> saveSku(Long id, List<PmsProductSku> pmsProductSkuList);

    CommonResult<Page<PmsProductSku>> list(Integer page, Integer pageSize);

    CommonResult<Page<PmsProductSku>> search(PmsSkuSearchDTO searchDTO);

    CommonResult<String> saveSku(PmsProductSku sku);

    CommonResult<String> updateSku(Long id, PmsProductSku sku);

    CommonResult<String> deleteSku(List<Long> ids);

    CommonResult<String> upload(MultipartFile file);

    CommonResult<String> batchUpdateStatus(List<Long> ids, Integer status);

    CommonResult<String> updateStatus(Long id, Integer status);

    CommonResult<List<SysOperationLog>> getSkuLogs(Long skuId);

    CommonResult<List<PmsSkuPriceHistory>> getPriceHistory(Long skuId);
}
