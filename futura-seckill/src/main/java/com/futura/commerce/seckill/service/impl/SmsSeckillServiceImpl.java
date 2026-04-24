package com.futura.commerce.seckill.service.impl;

import com.futura.commerce.seckill.dto.ActivitySearchDTO;
import com.futura.commerce.seckill.dto.SmsSeckillUpdateDTO;
import com.futura.commerce.seckill.service.SmsSeckillService;
import com.futura.commerce.seckill.vo.SmsFlashSaleVO;
import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.mbg.model.PmsProductSku;
import com.futura.commerce.mbg.model.SmsActivity;
import com.futura.commerce.mbg.model.SmsSeckill;
import com.futura.commerce.mbg.repository.PmsProductSkuRepository;
import com.futura.commerce.mbg.repository.SmsActivityRepository;
import com.futura.commerce.mbg.repository.SmsSeckillRepository;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementation for flash sale promotions
 *
 * @author Vitalii
 */
@Service
public class SmsSeckillServiceImpl implements SmsSeckillService {

    private static final Logger log = LoggerFactory.getLogger(SmsSeckillServiceImpl.class);

    @Resource
    private SmsSeckillRepository seckillRepository;

    @Resource
    private SmsActivityRepository activityRepository;

    @Resource
    private PmsProductSkuRepository productSkuRepository;

    @Override
    public CommonResult<Page<SmsFlashSaleVO>> getSkillList(Integer pageNum, Integer pageSize, Integer type) {
        int page = (pageNum != null && pageNum > 0) ? pageNum - 1 : 0;
        int size = (pageSize != null && pageSize > 0) ? pageSize : 10;
        Pageable pageable = PageRequest.of(page, size);

        List<SmsActivity> activities = activityRepository.findAll().stream()
                .filter(a -> type == null || Objects.equals(a.getType(), type))
                .collect(Collectors.toList());

        if (activities.isEmpty()) {
            return CommonResult.success(new PageImpl<>(List.of(), pageable, 0), "No flash sale activities found");
        }

        Map<Long, SmsActivity> activityMap = activities.stream()
                .collect(Collectors.toMap(SmsActivity::getId, a -> a, (k1, k2) -> k1));

        List<SmsSeckill> seckills = seckillRepository.findAll().stream()
                .filter(s -> s.getActivityId() != null && activityMap.containsKey(s.getActivityId()))
                .collect(Collectors.toList());

        if (seckills.isEmpty()) {
            return CommonResult.success(new PageImpl<>(List.of(), pageable, 0), "No flash sale items found");
        }

        List<Long> skuIds = seckills.stream().map(SmsSeckill::getSkuId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, PmsProductSku> skuMap = productSkuRepository.findAllById(skuIds).stream()
                .collect(Collectors.toMap(PmsProductSku::getId, s -> s, (k1, k2) -> k1));

        List<SmsFlashSaleVO> voList = seckills.stream().map(s -> {
            SmsActivity activity = activityMap.get(s.getActivityId());
            PmsProductSku sku = skuMap.get(s.getSkuId());

            SmsFlashSaleVO vo = new SmsFlashSaleVO();
            vo.setId(s.getId());
            vo.setSkuId(s.getSkuId());
            vo.setSeckillPrice(s.getSeckillPrice());
            vo.setStock(s.getStock());
            vo.setSoldStock(s.getSoldStock());
            vo.setLimitQuantity(s.getLimitQuantity());
            vo.setStockStatus(s.getStockStatus());

            if (sku != null) {
                vo.setOriginalPrice(sku.getPrice());
                vo.setProductName(sku.getModel());
                vo.setPic(sku.getPic());
            }

            if (activity != null) {
                vo.setStartTime(activity.getStartTime());
                vo.setEndTime(activity.getEndTime());
            }

            return vo;
        }).collect(Collectors.toList());

        int total = voList.size();
        int fromIndex = Math.min(page * size, total);
        int toIndex = Math.min(fromIndex + size, total);
        List<SmsFlashSaleVO> pageContent = voList.subList(fromIndex, toIndex);

        return CommonResult.success(new PageImpl<>(pageContent, pageable, total), "Fetched flash sale list successfully");
    }

    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult<SmsSeckillUpdateDTO> skillEdit(Long id, SmsSeckillUpdateDTO dto) {
        try {
            if (dto == null) {
                return CommonResult.failed("Request body is empty");
            }
            Long activityId = dto.getActivityId();
            if (activityId != null) {
                Optional<SmsActivity> actOpt = activityRepository.findById(activityId);
                if (actOpt.isPresent()) {
                    SmsActivity activity = actOpt.get();
                    if (dto.getTitle() != null) {
                        activity.setTitle(dto.getTitle());
                    }
                    if (dto.getStartTime() != null) {
                        activity.setStartTime(dto.getStartTime());
                    }
                    if (dto.getEndTime() != null) {
                        activity.setEndTime(dto.getEndTime());
                    }
                    if (dto.getDescription() != null) {
                        activity.setDescription(dto.getDescription());
                    }
                    if (dto.getUserLevelLimit() != null && !dto.getUserLevelLimit().isEmpty()) {
                        String[] levels = dto.getUserLevelLimit().split(",");
                        activity.setUserLevelLimit(Integer.parseInt(levels[0].trim()));
                    }
                    if (dto.getOrderTypeLimit() != null && !dto.getOrderTypeLimit().isEmpty()) {
                        String[] types = dto.getOrderTypeLimit().split(",");
                        activity.setOrderTypeLimit(Integer.parseInt(types[0].trim()));
                    }
                    activityRepository.save(activity);
                }
            }

            if (id != null) {
                Optional<SmsSeckill> seckillOpt = seckillRepository.findById(id);
                if (seckillOpt.isPresent()) {
                    SmsSeckill seckill = seckillOpt.get();
                    if (dto.getSkuId() != null) {
                        seckill.setSkuId(dto.getSkuId());
                    }
                    if (dto.getSeckillPrice() != null) {
                        seckill.setSeckillPrice(dto.getSeckillPrice());
                    }
                    if (dto.getStock() != null) {
                        seckill.setStock(dto.getStock());
                    }
                    if (dto.getLimitQuantity() != null) {
                        seckill.setLimitQuantity(dto.getLimitQuantity());
                    }
                    seckillRepository.save(seckill);
                }
            }

            return CommonResult.success(dto, "Flash sale promotion updated successfully");
        } catch (Exception e) {
            log.error("Failed to edit flash sale", e);
            return CommonResult.failed("Failed to edit flash sale: " + e.getMessage());
        }
    }

    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult<String> skillDelete(Long id) {
        try {
            Optional<SmsSeckill> seckillOpt = seckillRepository.findById(id);
            if (seckillOpt.isEmpty()) {
                return CommonResult.failed("Flash sale does not exist");
            }
            SmsSeckill seckill = seckillOpt.get();
            seckillRepository.deleteById(id);
            if (seckill.getActivityId() != null) {
                activityRepository.deleteById(seckill.getActivityId());
            }
            return CommonResult.success(null, "Deleted successfully");
        } catch (Exception e) {
            log.error("Failed to delete flash sale", e);
            return CommonResult.failed("Failed to delete flash sale: " + e.getMessage());
        }
    }

    @org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
    @Override
    public CommonResult<String> skillDeleteBatch(Long[] ids) {
        try {
            if (ids == null || ids.length == 0) {
                return CommonResult.failed("No IDs specified for deletion");
            }
            List<Long> idList = Arrays.asList(ids);
            List<SmsSeckill> seckills = seckillRepository.findAllById(idList);
            List<Long> activityIds = seckills.stream()
                    .map(SmsSeckill::getActivityId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            seckillRepository.deleteAllById(idList);
            if (!activityIds.isEmpty()) {
                activityRepository.deleteAllById(activityIds);
            }
            return CommonResult.success(null, "Batch deleted successfully");
        } catch (Exception e) {
            log.error("Failed to batch delete flash sales", e);
            return CommonResult.failed("Batch deletion failed: " + e.getMessage());
        }
    }

    @Override
    public CommonResult<ActivitySearchDTO> skillSearch(ActivitySearchDTO searchDTO) {
        try {
            CommonResult<Page<SmsFlashSaleVO>> resultVO = getSkillList(1, 1000, 1);
            List<SmsFlashSaleVO> list = (resultVO.getData() != null) ? resultVO.getData().getContent() : List.of();

            List<SmsFlashSaleVO> filteredList = list.stream()
                    .filter(item -> {
                        if (searchDTO.getActivityName() != null && !searchDTO.getActivityName().isEmpty()) {
                            if (item.getActivityName() == null || !item.getActivityName().contains(searchDTO.getActivityName())) {
                                return false;
                            }
                        }
                        if (searchDTO.getId() != null) {
                            if (!searchDTO.getId().equals(item.getId())) {
                                return false;
                            }
                        }
                        if (searchDTO.getStartTime() != null) {
                            if (item.getStartTime() == null || item.getStartTime().isBefore(searchDTO.getStartTime())) {
                                return false;
                            }
                        }
                        if (searchDTO.getEndTime() != null) {
                            if (item.getEndTime() == null || item.getEndTime().isAfter(searchDTO.getEndTime())) {
                                return false;
                            }
                        }
                        if (searchDTO.getProductName() != null && !searchDTO.getProductName().isEmpty()) {
                            if (item.getProductName() == null || !item.getProductName().contains(searchDTO.getProductName())) {
                                return false;
                            }
                        }
                        if (searchDTO.getSeckillPrice() != null) {
                            if (item.getSeckillPrice() == null || item.getSeckillPrice().compareTo(searchDTO.getSeckillPrice()) < 0) {
                                return false;
                            }
                        }
                        if (searchDTO.getStockStatus() != null) {
                            if (!searchDTO.getStockStatus().equals(item.getStockStatus())) {
                                return false;
                            }
                        }
                        return true;
                    })
                    .collect(Collectors.toList());

            ActivitySearchDTO result = new ActivitySearchDTO();
            result.setId(filteredList.isEmpty() ? null : filteredList.get(0).getId());

            return CommonResult.success(result, "Search completed successfully");
        } catch (Exception e) {
            log.error("Failed to search flash sales", e);
            return CommonResult.failed("Search failed: " + e.getMessage());
        }
    }
}
