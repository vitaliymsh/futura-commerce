package com.futura.commerce.admin.controller;

import com.futura.commerce.common.api.CommonResult;
import com.futura.commerce.feign.ai.AiFeignClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Administrative AI proxy controller forwarding requests to futura-ai service
 *
 * @author Vitalii
 */
@RestController
@Tag(name = "AdminAiController", description = "Back-office AI assistant proxy")
@RequiredArgsConstructor
@RequestMapping("/ai")
public class AdminAiController {

    private final AiFeignClient aiFeignClient;

    @GetMapping("/admin/chat")
    @Operation(summary = "Admin AI chat assistant", description = "Query AI assistant for order or catalog details")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('order:view','product:view')")
    public CommonResult<String> ai(@RequestParam String memoryId, @RequestParam String message) {
        return aiFeignClient.ai(memoryId, message);
    }

    @GetMapping("/admin/chat/history")
    @Operation(summary = "Admin chat history", description = "Retrieve previous chat interactions by memoryId")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('order:view','product:view')")
    public CommonResult<List<String>> getChatHistory(@RequestParam String memoryId) {
        return aiFeignClient.getChatHistory(memoryId);
    }

    @GetMapping("/admin/chat2")
    @Operation(summary = "Admin AI chat2", description = "Alternative chat query")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('order:view','product:view')")
    public CommonResult<String> ai2(@RequestParam String memoryId, @RequestParam String message) {
        return aiFeignClient.chat2(memoryId, message);
    }

    @GetMapping("/admin/chat3")
    @Operation(summary = "Admin AI chat3", description = "Stateless quick inquiry")
    @PreAuthorize("hasRole('ADMIN') or hasAnyAuthority('order:view','product:view')")
    public CommonResult<String> ai3(@RequestParam String message) {
        return aiFeignClient.chat3(message);
    }
}
