package com.opentext.chatbot_backend.controller;

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

import com.opentext.chatbot_backend.requestDTO.ChatRequest;
import com.opentext.chatbot_backend.responseDTO.ChatResponse;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

	@Value("${openai.api.key}")
	private String apiKey;

	private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

	@PostMapping
	public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(apiKey);
			headers.setContentType(MediaType.APPLICATION_JSON);

			Map<String, Object> message = Map.of("role", "user", "content", request.getPrompt());

			Map<String, Object> body = Map.of("model", "gpt-4.1", "messages", List.of(message));

			HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

			ResponseEntity<Map> response = restTemplate.postForEntity(OPENAI_URL, entity, Map.class);

			List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
			Map<String, Object> messages = (Map<String, Object>) choices.get(0).get("message");
			String reply = (String) messages.get("content");

			return ResponseEntity.ok(new ChatResponse(reply));
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ChatResponse("Something went wrong: " + ex.getMessage()));
		}
	}
}