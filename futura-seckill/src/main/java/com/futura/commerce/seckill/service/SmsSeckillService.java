package com.futura.commerce.seckill.service;

import com.futura.commerce.seckill.dto.ActivitySearchDTO;
import com.futura.commerce.seckill.dto.SmsSeckillUpdateDTO;
import com.futura.commerce.seckill.vo.SmsFlashSaleVO;
import com.futura.commerce.common.api.CommonResult;
import org.springframework.data.domain.Page;

/**
 * Service interface for flash sale (seckill) promotions
 *
 * @author Vitalii
 */
public interface SmsSeckillService {

    CommonResult<Page<SmsFlashSaleVO>> getSkillList(Integer pageNum, Integer pageSize, Integer type);

    CommonResult<SmsSeckillUpdateDTO> skillEdit(Long id, SmsSeckillUpdateDTO smsSeckill);

    CommonResult<String> skillDelete(Long id);

    CommonResult<String> skillDeleteBatch(Long[] ids);

    CommonResult<ActivitySearchDTO> skillSearch(ActivitySearchDTO smsSeckill);
}
