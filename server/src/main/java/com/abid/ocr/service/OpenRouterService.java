package com.abid.ocr.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.abid.ocr.config.OpenRouterConfig;
import com.abid.ocr.dto.OpenRouterRequest;
import com.abid.ocr.dto.OpenRouterRequest.Message;
import com.abid.ocr.dto.OpenRouterRequest.Message.Content;

@Service
public class OpenRouterService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final OpenRouterConfig openRouterConfig;

    public OpenRouterService(OpenRouterConfig openRouterConfig) {
        this.openRouterConfig = openRouterConfig;
    }

    public String sendImageQuery(String imageUrl, String userPrompt) {
        Content textPart = new Content();
        textPart.setType("text");
        textPart.setText(userPrompt);

        Content imagePart = new Content();
        imagePart.setType("image_url");
        imagePart.setImage_url(Map.of("url", imageUrl));

        // Build message
        Message message = new Message();
        message.setRole("user");
        message.setContent(List.of(textPart, imagePart));

        // Build request
        OpenRouterRequest request = new OpenRouterRequest();
        request.setModel(openRouterConfig.getModel());
        request.setMessages(List.of(message));

        // Build headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openRouterConfig.getApiKey());

        HttpEntity<OpenRouterRequest> httpEntity = new HttpEntity<>(request, headers);

        String API_URL = openRouterConfig.getApiUrl();
        // Call API
        ResponseEntity<String> response = restTemplate.postForEntity(
                API_URL,
                httpEntity,
                String.class);

        return response.getBody(); // Handle parsing if needed

    }

}
