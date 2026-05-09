package com.futura.commerce.ai.controller;

import com.futura.commerce.ai.service.AiService;
import com.futura.commerce.ai.service.ChatService;
import com.futura.commerce.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller exposing AI chat assistance endpoints
 *
 * @author Vitalii
 */
@RestController
@RequestMapping("/ai")
@Tag(name = "AiController", description = "AI Assistant and Chat Operations")
@RequiredArgsConstructor
public class AiController {

    private final ChatService chatService;
    private final AiService aiService;

    @GetMapping("/admin/chat")
    @Operation(summary = "Admin back-office AI chat", description = "Query assistant with conversation memory")
    public CommonResult<String> chat(@RequestParam String memoryId, @RequestParam String message) {
        String response = chatService.chat(memoryId, message);
        return CommonResult.success(response, "success");
    }

    @GetMapping("/admin/chat/history")
    @Operation(summary = "Chat history", description = "Retrieve conversation history from Redis by memoryId")
    public CommonResult<List<String>> chatHistory(@RequestParam String memoryId) {
        List<String> history = chatService.getChatHistory(memoryId);
        return CommonResult.success(history, "success");
    }

    @GetMapping("/admin/chat2")
    @Operation(summary = "Alternative admin AI chat", description = "Secondary chat implementation with tool calling")
    public CommonResult<String> chat2(@RequestParam String memoryId, @RequestParam String message) {
        String response = chatService.chat2(memoryId, message);
        return CommonResult.success(response, "success");
    }

    @GetMapping("/admin/chat3")
    @Operation(summary = "Stateless quick chat", description = "Quick stateless message evaluation")
    public CommonResult<String> chat3(@RequestParam String message) {
        String response = aiService.msg3(message);
        return CommonResult.success(response, "success");
    }
}
