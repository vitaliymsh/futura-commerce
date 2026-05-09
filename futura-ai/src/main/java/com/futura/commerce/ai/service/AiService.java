package com.futura.commerce.ai.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.futura.commerce.ai.constants.AiPrompt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// copilot ai completion service
@Slf4j
@Service
public class AiService {

    @Value("${ai.enabled:false}")
    private boolean aiEnabled;

    @Value("${ai.ollama.base-url:http://localhost:11434}")
    private String ollamaBaseUrl;

    @Value("${ai.ollama.chat-model:qwen3-vl:4b}")
    private String chatModel;

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public AiService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.restClient = RestClient.builder().build();
    }

    public String msg(String memoryId, String question, String ragContext) {
        if (!aiEnabled) {
            log.info("AI provider is disabled (ai.enabled=false). Serving rule-based/RAG diagnostic response.");
            return generateOfflineResponse(question, ragContext);
        }

        try {
            log.info("Invoking local Ollama model '{}' at {}", chatModel, ollamaBaseUrl);

            String systemPrompt = AiPrompt.SYSTEM_ADMIN_AI_PROMPT;
            if (ragContext != null && !ragContext.isBlank()) {
                systemPrompt += "\n\n[Verified Reference Knowledge & Telemetry Data]:\n" + ragContext;
            }

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", chatModel);
            requestBody.put("stream", false);
            requestBody.put("messages", List.of(
                    Map.of("role", "system", "content", systemPrompt),
                    Map.of("role", "user", "content", question)
            ));

            String responseJson = restClient.post()
                    .uri(ollamaBaseUrl + "/api/chat")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            if (responseJson != null) {
                JsonNode root = objectMapper.readTree(responseJson);
                String reply = root.path("message").path("content").asText();
                if (!reply.isBlank()) {
                    return reply;
                }
            }
        } catch (Exception e) {
            log.warn("Ollama AI connection failed ({}); falling back to diagnostic engine: {}",
                    ollamaBaseUrl, e.getMessage());
        }

        return generateOfflineResponse(question, ragContext);
    }

    public String msg(String memoryId, String question) {
        return msg(memoryId, question, null);
    }

    public String msg2(String memoryId, String message) {
        return msg(memoryId, message, null);
    }

    public String msg3(String message) {
        return msg("default", message, null);
    }

    private String generateOfflineResponse(String question, String ragContext) {
        StringBuilder sb = new StringBuilder();
        if (ragContext != null && !ragContext.isBlank()) {
            sb.append(ragContext).append("\n\n");
        }
        sb.append("Futura Copilot [Offline / Rule Mode]: Query received for \"").append(question).append("\".\n");
        sb.append("Tip: Enable local Ollama (`AI_ENABLED=true`) to activate dynamic neural reasoning.");
        return sb.toString();
    }
}
