package com.futura.commerce.product.service.impl;

import com.futura.commerce.product.dto.CreatGoodsQuotaDTO;
import com.futura.commerce.product.dto.GoodsQuotaRequestVO;
import com.futura.commerce.product.dto.SmsPromotionProductVO;
import com.futura.commerce.product.service.PmsProductService;
import com.futura.commerce.product.service.SmsPromotionService;
import com.futura.commerce.mbg.repository.UmsAdminRepository;
import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.PmsProduct;
import com.futura.commerce.mbg.model.SmsPromotion;
import com.futura.commerce.mbg.model.SmsPromotionQuotaLog;
import com.futura.commerce.mbg.model.UmsAdmin;
import com.futura.commerce.mbg.repository.PmsProductRepository;
import com.futura.commerce.mbg.repository.SmsPromotionQuotaLogRepository;
import com.futura.commerce.mbg.repository.SmsPromotionRepository;
import com.futura.commerce.security.dto.LoginUser;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Service implementation for campaign promotions and quota allocation
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

    @Resource
    private PmsProductService pmsProductService;

    @Resource
    private UmsAdminRepository umsAdminRepository;

    @Resource
    private SmsPromotionQuotaLogRepository smsPromotionQuotaLogRepository;

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
                vo.setQuota(sp.getQuota() != null ? sp.getQuota().longValue() : 0L);
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<List<SmsPromotionProductVO>> createGoodsQuota(GoodsQuotaRequestVO goodsPromotionVO, Long packageId) {
        if (goodsPromotionVO == null || goodsPromotionVO.getGoods() == null || goodsPromotionVO.getGoods().isEmpty()) {
            return CommonResult.failed("Invalid goods promotion request parameters");
        }

        Long adminId = getCurrentAdminId();
        if (adminId == null) {
            return CommonResult.unauthorized("Authentication required");
        }

        Optional<UmsAdmin> adminOpt = umsAdminRepository.findById(adminId);
        if (adminOpt.isEmpty()) {
            return CommonResult.failed("Admin user not found");
        }
        UmsAdmin admin = adminOpt.get();

        List<SmsPromotionProductVO> list = new ArrayList<>();
        List<CreatGoodsQuotaDTO> goods = goodsPromotionVO.getGoods();

        for (CreatGoodsQuotaDTO vo : goods) {
            Long quota = vo.getQuota();
            Long id = vo.getId();

            if (id == null || quota == null) {
                return CommonResult.failed("Product ID and quota must not be null");
            }

            Optional<PmsProduct> productOpt = pmsProductService.findById(id);
            if (productOpt.isEmpty()) {
                return CommonResult.failed("Product not found: " + id);
            }
            PmsProduct product = productOpt.get();

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime endTime = now.plusDays(quota);
            long validDays = ChronoUnit.DAYS.between(now, endTime);

            // check if product is actively promoted
            Optional<SmsPromotion> existPromotionOpt = smsPromotionRepository.findFirstByProductIdAndStatus(id, 1);

            SmsPromotionProductVO promotionProductVO = new SmsPromotionProductVO();
            promotionProductVO.setProductId(product.getId());
            promotionProductVO.setName(product.getName());
            promotionProductVO.setPic(product.getPic());
            promotionProductVO.setPrice(product.getPrice());
            promotionProductVO.setStatus(1);
            promotionProductVO.setIsPromotion(1);
            promotionProductVO.setStatusDesc("Promoted");
            promotionProductVO.setPayTime(now);
            promotionProductVO.setQuota(quota);
            promotionProductVO.setValidDays(validDays);

            if (existPromotionOpt.isPresent()) {
                SmsPromotion existPromotion = existPromotionOpt.get();
                long currentQuota = existPromotion.getQuota() != null ? existPromotion.getQuota() : 0L;
                int currentDays = existPromotion.getDays() != null ? existPromotion.getDays() : 0;
                existPromotion.setQuota((quota != null ? quota : 0L) + currentQuota);
                existPromotion.setDays(Math.toIntExact(validDays) + currentDays);
                existPromotion.setEndTime(endTime);
                existPromotion.setPayTime(now);
                existPromotion.setStartTime(now);
                smsPromotionRepository.save(existPromotion);
                promotionProductVO.setStatusDesc("Promotion updated");
            } else {
                SmsPromotion smsPromotion = new SmsPromotion();
                smsPromotion.setProductId(id);
                smsPromotion.setAdminId(adminId);
                smsPromotion.setCategoryId(product.getCategoryId());
                smsPromotion.setPayAmount(product.getPrice());
                smsPromotion.setQuota(quota != null ? quota : 0L);
                smsPromotion.setStatus(1);
                smsPromotion.setDays(Math.toIntExact(validDays));
                smsPromotion.setPromotionType(packageId != null ? Math.toIntExact(packageId) : 0);
                smsPromotion.setIsCategory(0);
                smsPromotion.setPayTime(now);
                smsPromotion.setStartTime(now);
                smsPromotion.setEndTime(endTime);
                smsPromotionRepository.save(smsPromotion);
            }

            // save quota operation log
            SmsPromotionQuotaLog quotaLog = new SmsPromotionQuotaLog();
            quotaLog.setAdminId(adminId);
            quotaLog.setPromotionId(packageId);
            quotaLog.setProductId(id);
            quotaLog.setAddQuota(quota.intValue());
            quotaLog.setCreateTime(LocalDateTime.now());
            quotaLog.setStatus(0); // active
            smsPromotionQuotaLogRepository.save(quotaLog);

            list.add(promotionProductVO);

            // deduct admin promotion quota
            long totalQuota = admin.getPromotionQuota() != null ? admin.getPromotionQuota() : 0L;
            long usedQuota = admin.getUsedPromotionQuota() != null ? admin.getUsedPromotionQuota() : 0L;
            admin.setPromotionQuota(totalQuota - quota);
            admin.setUsedPromotionQuota(usedQuota + quota);
            umsAdminRepository.save(admin);
        }

        log.info("Promotion created successfully for items: {}", list);
        return CommonResult.success(list, "Product promotion quota created successfully");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<String> delGoods(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return CommonResult.failed("Product IDs list must not be empty");
        }

        Long adminId = getCurrentAdminId();
        if (adminId == null) {
            return CommonResult.unauthorized("Authentication required");
        }

        Optional<UmsAdmin> adminOpt = umsAdminRepository.findById(adminId);
        if (adminOpt.isEmpty()) {
            return CommonResult.failed("Admin user not found");
        }
        UmsAdmin admin = adminOpt.get();

        LocalDateTime now = LocalDateTime.now();

        for (Long productId : productIds) {
            // 1. fetch all active unrevoked promotion quota logs for this product
            List<SmsPromotionQuotaLog> logList = smsPromotionQuotaLogRepository.findByProductIdAndStatus(productId, 0);

            // 2. lookup existing promotion records for package type reference
            List<SmsPromotion> promotions = smsPromotionRepository.findByProductId(productId);

            for (SmsPromotion promotion : promotions) {
                int totalCanRecoverQuota = 0;

                // check if log was created within 5 minutes
                for (SmsPromotionQuotaLog qLog : logList) {
                    LocalDateTime logCreateTime = qLog.getCreateTime();
                    LocalDateTime expireTime = logCreateTime != null ? logCreateTime.plusMinutes(5) : now.minusMinutes(1);

                    // only logs within 5 minutes are eligible for quota recovery
                    if (expireTime.isAfter(now)) {
                        totalCanRecoverQuota += (qLog.getAddQuota() != null ? qLog.getAddQuota() : 0);

                        // mark log as revoked
                        qLog.setStatus(1);
                        smsPromotionQuotaLogRepository.save(qLog);
                    }
                }

                // 3. recover quota to admin if any within grace period
                if (totalCanRecoverQuota > 0) {
                    long currentQuota = admin.getPromotionQuota() != null ? admin.getPromotionQuota() : 0L;
                    long usedQuota = admin.getUsedPromotionQuota() != null ? admin.getUsedPromotionQuota() : 0L;

                    admin.setPromotionQuota(currentQuota + totalCanRecoverQuota);
                    admin.setUsedPromotionQuota(usedQuota - totalCanRecoverQuota);
                    umsAdminRepository.save(admin);

                    // record recovery log entry (negative quota represents refund)
                    SmsPromotionQuotaLog recoverLog = new SmsPromotionQuotaLog();
                    recoverLog.setPromotionId(promotion.getPromotionType() != null ? promotion.getPromotionType().longValue() : null);
                    recoverLog.setAdminId(adminId);
                    recoverLog.setProductId(productId);
                    recoverLog.setAddQuota(-totalCanRecoverQuota);
                    recoverLog.setCreateTime(now);
                    recoverLog.setStatus(1);
                    smsPromotionQuotaLogRepository.save(recoverLog);
                }
            }

            // 4. delete promotion record
            log.info("Deleting promotion record for product: {}", productId);
            smsPromotionRepository.deleteByProductId(productId);
        }

        return CommonResult.success("Promotion deleted successfully, eligible quota within 5 minutes refunded");
    }

    private Long getCurrentAdminId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser loginUser) {
            if (loginUser.getAdmin() != null) {
                return loginUser.getAdmin().getId();
            }
        }
        return null;
    }
}
