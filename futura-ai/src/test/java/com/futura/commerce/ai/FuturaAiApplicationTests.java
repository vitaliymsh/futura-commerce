package com.futura.commerce.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.futura.commerce.ai.service.AiService;
import com.futura.commerce.ai.service.ChatService;
import com.futura.commerce.ai.service.OrderToolService;
import com.futura.commerce.ai.service.ProductToolService;
import com.futura.commerce.ai.util.PdfParseUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class FuturaAiApplicationTests {

    @Test
    void testRAGKnowledgeParsing() {
        PdfParseUtil pdfParseUtil = new PdfParseUtil();
        pdfParseUtil.initKnowledgeBase();

        List<String> segments = pdfParseUtil.findRelevantSegments("refund return policy", 2);
        Assertions.assertNotNull(segments);
    }

    @Test
    void testAiServiceGracefulOfflineResponse() {
        AiService aiService = new AiService(new ObjectMapper());
        String response = aiService.msg("test-session", "What is the return policy?", "Return within 30 days");
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.contains("Return within 30 days") || response.contains("Futura Copilot"));
    }

    @Test
    void testChatServiceOrderAndProductRouting() {
        AiService aiService = new AiService(new ObjectMapper());
        OrderToolService stubOrderTool = new OrderToolService(null) {
            @Override
            public String getOrderInfo(String orderNo) {
                return "Order #" + orderNo + " Total: $199.00";
            }
        };
        ProductToolService stubProductTool = new ProductToolService(null, new ObjectMapper()) {
            @Override
            public String getProductDetail(Long productId) {
                return "Product #" + productId + ": Mechanical Keyboard";
            }

            @Override
            public String searchProducts(String keyword) {
                return "1. [ID: 5] Mechanical Keyboard - $199.00";
            }
        };
        PdfParseUtil pdfParseUtil = new PdfParseUtil();

        ChatService chatService = new ChatService(aiService, stubOrderTool, stubProductTool, pdfParseUtil, null);

        String orderResult = chatService.chat("session-1", "Please check ORDER10029");
        Assertions.assertEquals("Order #ORDER10029 Total: $199.00", orderResult);

        String productResult = chatService.chat("session-1", "Look up PRODUCT#5");
        Assertions.assertEquals("Product #5: Mechanical Keyboard", productResult);
    }
}
