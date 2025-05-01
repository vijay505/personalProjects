package com.opentext.groqPoc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.opentext.groqPoc.requestDTO.ChatRequest;
import com.opentext.groqPoc.responseDTO.ChatResponse;

@RestController
@RequestMapping("/api/groq")
public class GroqController {

	@Value("${groq.api.key}")
	private String apiKey;

	 private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";


    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "llama-3.1-8b-instant");
        body.put("messages", List.of(
            Map.of("role", "user", "content", request.getPrompt())
        ));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(GROQ_URL, entity, Map.class);
            Map<String, Object> choice = (Map)((List)response.getBody().get("choices")).get(0);
            Map<String, String> message = (Map<String, String>) choice.get("message");

            String reply = message.get("content");
            return ResponseEntity.ok(new ChatResponse(reply));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ChatResponse("Error: " + e.getMessage()));
        }
	}
}