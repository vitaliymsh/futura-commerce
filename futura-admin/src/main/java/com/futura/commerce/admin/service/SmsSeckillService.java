package com.futura.commerce.admin.service;

import com.futura.commerce.admin.vo.SmsFlashSaleVO;
import com.futura.commerce.common.api.CommonResult;
import org.springframework.data.domain.Page;

/**
 * Service interface for flash sale (seckill) promotions
 *
 * @author Vitalii
 */
public interface SmsSeckillService {

    CommonResult<Page<SmsFlashSaleVO>> getSkillList(Integer pageNum, Integer pageSize, Integer type);

    CommonResult<com.futura.commerce.admin.dto.SmsSeckillUpdateDTO> skillEdit(Long id, com.futura.commerce.admin.dto.SmsSeckillUpdateDTO smsSeckill);

    CommonResult<String> skillDelete(Long id);

    CommonResult<String> skillDeleteBatch(Long[] ids);

    CommonResult<com.futura.commerce.admin.dto.ActivitySearchDTO> skillSearch(com.futura.commerce.admin.dto.ActivitySearchDTO smsSeckill);
}
