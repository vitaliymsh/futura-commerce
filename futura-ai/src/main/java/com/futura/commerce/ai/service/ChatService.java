package com.futura.commerce.ai.service;

import com.futura.commerce.ai.constants.AiPrompt;
import com.futura.commerce.ai.util.PdfParseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// chat conversation orchestration with redis memory and tools
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final AiService aiService;
    private final OrderToolService orderToolService;
    private final ProductToolService productToolService;
    private final PdfParseUtil pdfParseUtil;
    private final StringRedisTemplate stringRedisTemplate;

    // entity regex patterns
    private static final Pattern ORDER_PATTERN = Pattern.compile("(?i)ORDER\\d+");
    private static final Pattern PRODUCT_PATTERN = Pattern.compile("(?i)(?:PRODUCT|PROD)#?(\\d+)");
    private static final Pattern SKU_PATTERN = Pattern.compile("(?i)SKU#?(\\d+)");
    private static final Pattern SEARCH_PATTERN = Pattern.compile("(?i)(?:search|find|lookup|show)\\s+(?:product|item|goods)?\\s*[:\"]?([a-zA-Z0-9\\s\\-_]+)[\"]?");

    public String chat(String memoryId, String question) {
        StringBuilder toolContext = new StringBuilder();

        // check order entity
        Matcher orderMatcher = ORDER_PATTERN.matcher(question);
        if (orderMatcher.find()) {
            String orderNo = orderMatcher.group().toUpperCase();
            log.info("Detected order query for: {}", orderNo);
            String orderInfo = orderToolService.getOrderInfo(orderNo);
            saveHistory(memoryId, question, orderInfo);
            return orderInfo;
        }

        // check product id
        Matcher productMatcher = PRODUCT_PATTERN.matcher(question);
        if (productMatcher.find()) {
            long productId = Long.parseLong(productMatcher.group(1));
            log.info("Detected product ID query: {}", productId);
            String productDetail = productToolService.getProductDetail(productId);
            saveHistory(memoryId, question, productDetail);
            return productDetail;
        }

        // check sku id
        Matcher skuMatcher = SKU_PATTERN.matcher(question);
        if (skuMatcher.find()) {
            long skuId = Long.parseLong(skuMatcher.group(1));
            log.info("Detected SKU ID query: {}", skuId);
            String skuDetail = productToolService.getSkuAuditTrail(skuId);
            saveHistory(memoryId, question, skuDetail);
            return skuDetail;
        }

        // search products in catalog
        Matcher searchMatcher = SEARCH_PATTERN.matcher(question);
        if (searchMatcher.find()) {
            String term = searchMatcher.group(1).trim();
            if (!term.isBlank() && !term.equalsIgnoreCase("order")) {
                log.info("Detected product keyword search: {}", term);
                String searchResults = productToolService.searchProducts(term);
                toolContext.append("[Live Product Catalog Search Results]:\n").append(searchResults).append("\n\n");
            }
        }

        // find rag policy segments
        List<String> ragSegments = pdfParseUtil.findRelevantSegments(question, 2);
        if (!ragSegments.isEmpty()) {
            toolContext.append("[Relevant Platform Operating Rules & Policies]:\n");
            for (String segment : ragSegments) {
                toolContext.append(segment).append("\n---\n");
            }
        }

        // invoke copilot ai service
        String answer = aiService.msg(memoryId, question, toolContext.toString().trim());
        saveHistory(memoryId, question, answer);
        return answer;
    }

    public List<String> getChatHistory(String memoryId) {
        List<String> history = stringRedisTemplate.opsForList().range(AiPrompt.CHAT_MEMORY + memoryId, 0, -1);
        if (history == null || history.isEmpty()) {
            return new ArrayList<>();
        }
        return history;
    }

    public String chat2(String memoryId, String question) {
        return chat(memoryId, question);
    }

    public String chat33(String memoryId, String message) {
        return chat(memoryId, message);
    }

    private void saveHistory(String memoryId, String question, String answer) {
        try {
            String key = AiPrompt.CHAT_MEMORY + memoryId;
            stringRedisTemplate.opsForList().rightPush(key, "User: " + question);
            stringRedisTemplate.opsForList().rightPush(key, "AI: " + answer);
        } catch (Exception e) {
            log.warn("Could not record chat history in Redis: {}", e.getMessage());
        }
    }
}
