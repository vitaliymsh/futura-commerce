package com.futura.commerce.feign.ai;

import com.futura.commerce.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Feign client interface for Futura AI microservice
 *
 * @author Vitalii
 */
@FeignClient(name = "futura-ai", url = "${futura.ai.url:http://localhost:8087}", contextId = "aiClient")
public interface AiFeignClient {

    @GetMapping("/ai/admin/chat")
    CommonResult<String> ai(@RequestParam("memoryId") String memoryId, @RequestParam("message") String message);

    @GetMapping("/ai/admin/chat/history")
    CommonResult<List<String>> getChatHistory(@RequestParam("memoryId") String memoryId);

    @GetMapping("/ai/admin/chat2")
    CommonResult<String> chat2(@RequestParam("memoryId") String memoryId, @RequestParam("message") String message);

    @GetMapping("/ai/admin/chat3")
    CommonResult<String> chat3(@RequestParam("message") String message);
}
