package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.dto.SmsPromotionProductVO;
import com.futura.commerce.admin.service.SmsPromotionService;
import com.futura.commerce.common.baseCommon.CommonResult;
import com.futura.commerce.mbg.model.PmsProduct;
import com.futura.commerce.mbg.model.SmsPromotion;
import com.futura.commerce.mbg.repository.PmsProductRepository;
import com.futura.commerce.mbg.repository.SmsPromotionRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Service implementation for campaign promotions
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class SmsPromotionServiceImpl implements SmsPromotionService {

    @Resource
    private SmsPromotionRepository smsPromotionRepository;

    @Resource
    private PmsProductRepository pmsProductRepository;

    @Override
    public List<SmsPromotion> findAll() {
        return smsPromotionRepository.findAll();
    }

    @Override
    public Optional<SmsPromotion> findById(Long id) {
        return smsPromotionRepository.findById(id);
    }

    @Override
    public SmsPromotion save(SmsPromotion entity) {
        return smsPromotionRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        smsPromotionRepository.deleteById(id);
    }

    @Override
    public CommonResult<List<SmsPromotionProductVO>> getPromotionProductList() {
        try {
            // 1. Fetch non-category promotions
            List<SmsPromotion> promotions = smsPromotionRepository.findByIsCategoryOrderByCreateTimeDesc(0);
            List<SmsPromotionProductVO> voList = new ArrayList<>();
            LocalDateTime now = LocalDateTime.now();

            for (SmsPromotion sp : promotions) {
                SmsPromotionProductVO vo = new SmsPromotionProductVO();
                vo.setQuota(sp.getQuota());
                vo.setStatus(sp.getStatus());
                vo.setPayTime(sp.getPayTime());
                vo.setCreateTime(sp.getCreateTime());

                if (sp.getProductId() != null) {
                    Optional<PmsProduct> productOpt = pmsProductRepository.findById(sp.getProductId());
                    if (productOpt.isPresent()) {
                        PmsProduct p = productOpt.get();
                        vo.setProductId(p.getId());
                        vo.setName(p.getName());
                        vo.setPic(p.getPic());
                        vo.setPrice(p.getPrice());
                        vo.setIsPromotion(p.getPublishStatus());
                    }
                }

                // 2. Calculate remaining valid days
                if (sp.getEndTime() != null && sp.getEndTime().isAfter(now)) {
                    long days = Duration.between(now, sp.getEndTime()).toDays();
                    vo.setValidDays(Math.max(days, 1L));
                } else {
                    vo.setValidDays(0L);
                }

                // 3. Format localized status description
                Integer status = sp.getStatus();
                if (status != null) {
                    if (status == 1) {
                        vo.setStatusDesc(vo.getValidDays() > 0 ? "Promoted" : "Expired");
                    } else if (status == 2) {
                        vo.setStatusDesc("Ended");
                    } else if (status == 3) {
                        vo.setStatusDesc("Cancelled");
                    } else {
                        vo.setStatusDesc("Unknown");
                    }
                } else {
                    vo.setStatusDesc("Pending");
                }

                voList.add(vo);
            }

            return CommonResult.success(voList, "Promotion products retrieved successfully");
        } catch (Exception e) {
            log.error("Failed to query promotion product list", e);
            return CommonResult.failed("Failed to query promotion products: " + e.getMessage());
        }
    }
}
