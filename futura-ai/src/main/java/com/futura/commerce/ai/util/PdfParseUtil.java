package com.futura.commerce.ai.util;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// doc parsing and rag segment indexing
@Slf4j
@Component
public class PdfParseUtil {

    @Value("classpath:ecommerce.txt")
    private Resource knowledgeResource;

    private final List<String> knowledgeSegments = new ArrayList<>();

    @PostConstruct
    public void initKnowledgeBase() {
        try {
            if (knowledgeResource != null && knowledgeResource.exists()) {
                try (InputStream in = knowledgeResource.getInputStream()) {
                    String fullText = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                    String[] sections = fullText.split("\\n(?=## )");
                    knowledgeSegments.clear();
                    for (String section : sections) {
                        String trimmed = section.trim();
                        if (!trimmed.isEmpty()) {
                            knowledgeSegments.add(trimmed);
                        }
                    }
                    log.info("Initialized RAG knowledge store with {} topical segments.", knowledgeSegments.size());
                }
            } else {
                log.info("No external knowledge resource found at classpath:ecommerce.txt; skipping RAG bootstrap.");
            }
        } catch (Exception e) {
            log.warn("Error bootstrapping RAG knowledge base: {}", e.getMessage());
        }
    }

    public List<String> findRelevantSegments(String query, int limit) {
        if (knowledgeSegments.isEmpty()) {
            return List.of();
        }
        String lowerQuery = query.toLowerCase();
        List<String> keywords = Arrays.stream(lowerQuery.split("\\W+"))
                .filter(w -> w.length() > 2)
                .toList();

        return knowledgeSegments.stream()
                .sorted((a, b) -> {
                    long scoreA = keywords.stream().filter(k -> a.toLowerCase().contains(k)).count();
                    long scoreB = keywords.stream().filter(k -> b.toLowerCase().contains(k)).count();
                    return Long.compare(scoreB, scoreA);
                })
                .limit(limit)
                .toList();
    }

    public List<String> getAllSegments() {
        return List.copyOf(knowledgeSegments);
    }
}
