package com.opentext.deepgramAudioConv.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.opentext.TranscriptionResponseDTO;

@Service("deepgramService")
public class DeepgramService {

    @Value("${deepgram.api.key}")
    private String apiKey;

    @Value("${deepgram.api.url}")
    private String apiUrl;
    
    public TranscriptionResponseDTO transcribe(MultipartFile file) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(file.getContentType()));
        headers.set("Authorization", "Token " + apiKey);

        HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // Parse the response to extract the transcript
        return extractTranscript(response.getBody());
    }

    private TranscriptionResponseDTO extractTranscript(String jsonResponse) throws JSONException {
        // Parse the JSON response to get the transcript
        JSONObject json = new JSONObject(jsonResponse);
        String transcript = json
                .getJSONObject("results")
                .getJSONArray("channels")
                .getJSONObject(0)
                .getJSONArray("alternatives")
                .getJSONObject(0)
                .getString("transcript");

        // Return a DTO with the transcript
        return new TranscriptionResponseDTO(transcript);
    }
}