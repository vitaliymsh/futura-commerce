package com.futura.commerce.ai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.feign.product.ProductFeignClient;
import com.futura.commerce.mbg.model.PmsSkuPriceHistory;
import com.futura.commerce.mbg.model.SysOperationLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

// product and sku tool query service
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductToolService {

    private final ProductFeignClient productFeignClient;
    private final ObjectMapper objectMapper;

    public String searchProducts(String keyword) {
        try {
            CommonResult<?> result = productFeignClient.search(1, 8, null, keyword, null);
            if (result == null || result.getData() == null) {
                return "No products found matching query: \"" + keyword + "\"";
            }

            JsonNode dataNode = objectMapper.valueToTree(result.getData());
            JsonNode listNode = dataNode.has("list") ? dataNode.get("list") : (dataNode.has("content") ? dataNode.get("content") : dataNode);

            if (!listNode.isArray() || listNode.isEmpty()) {
                return "No products found matching query: \"" + keyword + "\"";
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Product Search Results for [").append(keyword).append("]:\n");
            int count = 1;
            for (JsonNode item : listNode) {
                long id = item.path("id").asLong();
                String name = item.path("name").asText(item.path("title").asText("N/A"));
                double price = item.path("price").asDouble(0.0);
                String subTitle = item.path("subTitle").asText("");
                int status = item.path("publishStatus").asInt(1);

                sb.append(String.format("%d. [ID: %d] %s - $%.2f (%s)\n", count++, id, name, price, status == 1 ? "Active" : "Archived"));
                if (!subTitle.isBlank()) {
                    sb.append("   Features: ").append(subTitle).append("\n");
                }
            }
            return sb.toString();
        } catch (Exception e) {
            log.warn("Failed to search products via Feign client: {}", e.getMessage());
            return "Product search temporarily unavailable: " + e.getMessage();
        }
    }

    public String getProductDetail(Long productId) {
        try {
            CommonResult<?> result = productFeignClient.detail(productId);
            if (result == null || result.getData() == null) {
                return "Product ID #" + productId + " not found.";
            }

            JsonNode node = objectMapper.valueToTree(result.getData());
            StringBuilder sb = new StringBuilder();
            sb.append("Product Details [ID: #").append(productId).append("]:\n");

            JsonNode productInfo = node.has("product") ? node.get("product") : node;
            sb.append("Name: ").append(productInfo.path("name").asText("N/A")).append("\n");
            sb.append("Base Price: $").append(productInfo.path("price").asDouble(0.0)).append("\n");
            sb.append("Brand: ").append(productInfo.path("brandName").asText("Futura Brand")).append("\n");
            sb.append("Category: ").append(productInfo.path("productCategoryName").asText("General")).append("\n");

            if (node.has("skuStockList") && node.get("skuStockList").isArray()) {
                sb.append("SKU Variants & Stock:\n");
                for (JsonNode sku : node.get("skuStockList")) {
                    sb.append(String.format("  - SKU #%d: Code=%s, Price=$%.2f, Stock=%d units\n",
                            sku.path("id").asLong(),
                            sku.path("skuCode").asText("N/A"),
                            sku.path("price").asDouble(0.0),
                            sku.path("stock").asInt(0)));
                }
            }
            return sb.toString();
        } catch (Exception e) {
            log.warn("Failed to get product detail for ID {}: {}", productId, e.getMessage());
            return "Product details lookup failed: " + e.getMessage();
        }
    }

    public String getSkuAuditTrail(Long skuId) {
        try {
            CommonResult<List<PmsSkuPriceHistory>> priceRes = productFeignClient.getPriceHistory(skuId);
            CommonResult<List<SysOperationLog>> logRes = productFeignClient.getSkuLogs(skuId);

            StringBuilder sb = new StringBuilder();
            sb.append("SKU #").append(skuId).append(" Telemetry & History:\n");

            if (priceRes != null && priceRes.getData() != null && !priceRes.getData().isEmpty()) {
                sb.append("Price Change History:\n");
                for (PmsSkuPriceHistory ph : priceRes.getData()) {
                    sb.append(String.format("  - %s: $%.2f -> $%.2f (by %s)\n",
                            ph.getUpdateTime(), ph.getOldPrice(), ph.getNewPrice(), ph.getOperator()));
                }
            }

            if (logRes != null && logRes.getData() != null && !logRes.getData().isEmpty()) {
                sb.append("Operation Audit Logs:\n");
                for (SysOperationLog ol : logRes.getData()) {
                    sb.append(String.format("  - %s: [%s] %s\n",
                            ol.getCreateTime(), ol.getOperationType(), ol.getContent()));
                }
            }

            return sb.length() > 30 ? sb.toString() : "No telemetry history recorded for SKU #" + skuId;
        } catch (Exception e) {
            log.warn("Failed to get SKU audit trail for {}: {}", skuId, e.getMessage());
            return "SKU audit trail query failed: " + e.getMessage();
        }
    }
}
