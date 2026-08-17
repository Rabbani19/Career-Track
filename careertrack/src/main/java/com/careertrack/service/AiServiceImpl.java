package com.careertrack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AiServiceImpl implements AiService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    @Value("${groq.model}")
    private String model;

    @Override
    public List<String> getInterviewTips(
            String jobRole, String companyName) {

        String prompt = "Give me 6 concise, practical interview tips "
                + "for a " + jobRole + " position"
                + (companyName != null && !companyName.isBlank()
                ? " at " + companyName : "") + ". "
                + "Return ONLY the tips as a plain numbered list "
                + "(1. 2. 3. ...), no intro, no closing remarks, "
                + "one tip per line.";

        String response = callGroq(
                "You are an expert career coach helping job seekers "
                        + "prepare for interviews.",
                prompt);

        return parseLines(response);
    }

    @Override
    public List<String> generateMockQuestions(
            String jobRole, String companyName) {

        String prompt = "Generate 8 realistic mock interview questions "
                + "for a " + jobRole + " position"
                + (companyName != null && !companyName.isBlank()
                ? " at " + companyName : "") + ". "
                + "Mix behavioral and technical/role-specific questions. "
                + "Return ONLY the questions as a plain numbered list "
                + "(1. 2. 3. ...), no intro, no closing remarks, "
                + "one question per line.";

        String response = callGroq(
                "You are an experienced technical interviewer and "
                        + "hiring manager.",
                prompt);

        return parseLines(response);
    }

    private String callGroq(
            String systemPrompt, String userPrompt) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", systemPrompt);

        Map<String, Object> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", userPrompt);

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", List.of(systemMessage, userMessage));
        body.put("temperature", 0.7);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    apiUrl, request, Map.class);

            List<Map<String, Object>> choices =
                    (List<Map<String, Object>>)
                            response.getBody().get("choices");

            Map<String, Object> message =
                    (Map<String, Object>) choices.get(0).get("message");

            return (String) message.get("content");

        } catch (Exception e) {
            throw new RuntimeException(
                    "AI request failed: " + e.getMessage());
        }
    }

    private List<String> parseLines(String content) {

        List<String> lines = new ArrayList<>();

        for (String line : content.split("\n")) {
            String cleaned = line.trim()
                    .replaceFirst("^\\d+[.)]\\s*", "")
                    .replaceFirst("^[-*]\\s*", "");

            if (!cleaned.isBlank()) {
                lines.add(cleaned);
            }
        }

        return lines;
    }
}