package com.futura.commerce.ai.service;

import com.futura.commerce.common.dto.AiOrderProductDto;
import com.futura.commerce.feign.order.OrderFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * AI tool service for retrieving and formatting order details via Feign RPC
 *
 * @author Vitalii
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderToolService {

    private final OrderFeignClient orderFeignClient;

    public String getOrderInfo(String orderNo) {
        try {
            AiOrderProductDto dto = orderFeignClient.getByOrderNo(orderNo);
            if (dto == null) {
                return "Order not found: " + orderNo;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Order Number: ").append(dto.getOrderNo()).append("\n");
            sb.append("Total Amount: $").append(dto.getTotalAmount()).append("\n");
            sb.append("Item Quantity: ").append(dto.getProductQuantity()).append("\n");
            sb.append("Products:\n");

            if (dto.getProductList() == null || dto.getProductList().isEmpty()) {
                sb.append("  - No items found for this order.");
            } else {
                dto.getProductList().forEach(product -> {
                    sb.append("  - ").append(product.getModel() != null ? product.getModel() : "Product")
                            .append(" (").append(product.getSpec() != null ? product.getSpec() : "Standard").append(")\n");
                    sb.append("    Price: $").append(product.getPrice()).append("\n");
                    sb.append("    Size/Attributes: ").append(product.getSize() != null ? product.getSize() : "N/A").append("\n");
                });
            }
            return sb.toString();
        } catch (Exception e) {
            log.error("Failed to query order details for AI prompt: {}", orderNo, e);
            return "Failed to query order: " + e.getMessage();
        }
    }
}
