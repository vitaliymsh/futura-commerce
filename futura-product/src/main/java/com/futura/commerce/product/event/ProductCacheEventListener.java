package com.futura.commerce.product.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.futura.commerce.mbg.model.PmsProductFeature;
import com.futura.commerce.mbg.model.ProductParams;
import com.futura.commerce.product.dto.ProductDetailDTO;
import com.futura.commerce.product.service.PmsProductFeatureService;
import com.futura.commerce.product.service.ProductParamsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Event listener for asynchronous product cache refresh and invalidation
 *
 * @author Vitalii
 */
@Slf4j
@Component
public class ProductCacheEventListener {

    @Resource
    private PmsProductFeatureService pmsProductFeatureService;

    @Resource
    private ProductParamsService productParamsService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ObjectMapper objectMapper;

    private static final String USER_DETAIL_KEY = "product:detail:";

    @Async
    @EventListener
    public void handleProductCacheEvict(ProductCacheEvictEvent event) {
        Long productId = event.getProductId();
        if (productId == null) {
            return;
        }

        try {
            List<PmsProductFeature> featureList = pmsProductFeatureService.findByProductId(productId);
            List<ProductParams> paramsList = productParamsService.findByProductsId(productId);

            ProductDetailDTO dto = new ProductDetailDTO();
            dto.setFeatureList(featureList);
            dto.setParamsList(paramsList);

            String json = objectMapper.writeValueAsString(dto);
            stringRedisTemplate.opsForValue().set(USER_DETAIL_KEY + productId, json, 1, TimeUnit.HOURS);
            log.info("Product cache asynchronously refreshed for productId: {}", productId);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize product detail DTO for productId: {}", productId, e);
            stringRedisTemplate.delete(USER_DETAIL_KEY + productId);
        } catch (Exception e) {
            log.error("Failed to asynchronously update product cache, evicting cache key for productId: {}", productId, e);
            stringRedisTemplate.delete(USER_DETAIL_KEY + productId);
        }
    }
}
