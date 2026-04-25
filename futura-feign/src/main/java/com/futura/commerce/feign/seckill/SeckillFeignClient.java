package com.futura.commerce.feign.seckill;

import com.futura.commerce.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Feign client interface for Futura Seckill & Marketing service
 *
 * @author Vitalii
 */
@FeignClient(name = "futura-seckill", url = "${futura.seckill.url:http://localhost:8085}", contextId = "seckillClient")
public interface SeckillFeignClient {

    @GetMapping("/promotion/balance")
    CommonResult<?> balance();

    @GetMapping("/promotion/recharge/list")
    CommonResult<?> rechargeList();

    @GetMapping("/promotion/list")
    CommonResult<?> promotionList();

    @PostMapping("/promotion/save")
    CommonResult<?> createGoodsQuota(@RequestBody Object dto);

    @DeleteMapping("/promotion/del")
    CommonResult<?> delGoodsQuota(@RequestParam List<Long> ids);

    // Flash sale / skill endpoints
    @PostMapping("/skill/update/{id}")
    CommonResult<?> skillEdit(@PathVariable Long id, @RequestBody Object smsSeckill);

    @DeleteMapping("/skill/delete/{id}")
    CommonResult<?> skillDelete(@PathVariable Long id);

    @DeleteMapping("/skill/delete/batch")
    CommonResult<?> skillDeleteBatch(@RequestBody Long[] ids);

    @PostMapping("/skill/search")
    CommonResult<?> skillSearch(@RequestBody Object smsSeckill);

    // Marketing activity endpoints
    @RequestMapping("/activity/smsStatus")
    CommonResult<?> activityStatus(@RequestParam Long id, @RequestParam(value = "smsStatus") Integer status);

    @RequestMapping("/activity/search")
    CommonResult<?> activitySearch(@RequestParam Integer pageNum,
                                   @RequestParam Integer pageSize,
                                   @RequestBody Object activitySearchDTO);

    @RequestMapping("/activity/list")
    CommonResult<?> activityList(@RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize,
                                 @RequestParam Integer type);
}
