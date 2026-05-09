package com.futura.commerce.product.service;

import com.futura.commerce.product.dto.IsPromotionDTO;
import com.futura.commerce.product.dto.PmsPromotionSearchDTO;
import com.futura.commerce.product.dto.PmsPromotionVO;
import com.futura.commerce.common.baseCommon.CommonResult;
import com.futura.commerce.mbg.model.PmsProduct;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing PmsProduct
 *
 * @author Vitalii
 */
public interface PmsProductService {

    List<PmsProduct> findAll();

    Optional<PmsProduct> findById(Long id);

    PmsProduct save(PmsProduct entity);

    void deleteById(Long id);

    CommonResult<String> isOpen(IsPromotionDTO promotionVO);

    CommonResult<List<PmsPromotionVO>> goodsList();

    CommonResult<Page<PmsPromotionVO>> goodsPagination(Integer page, Integer pageSize);

    CommonResult<Page<PmsProduct>> getPromotionByKeySearch(Integer page, Integer pageSize, Integer status, String keySearch, Integer categoryId);

    CommonResult<PmsPromotionSearchDTO> getPromotionSave(PmsPromotionSearchDTO promotion);

    CommonResult<String> upload(MultipartFile file);

    CommonResult<String> delete(Long id);

    CommonResult<String> updateStatus(Long id, Integer status);

    CommonResult<String> updateProduct(Long id, PmsPromotionSearchDTO promotion);

    CommonResult<com.futura.commerce.product.dto.ProductDetailDTO> detail(Long productId);
}
