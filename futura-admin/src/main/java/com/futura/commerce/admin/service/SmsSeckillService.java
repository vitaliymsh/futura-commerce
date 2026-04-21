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
}
