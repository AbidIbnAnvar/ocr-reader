package com.abid.ocr.service;

import java.util.Base64;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.abid.ocr.dto.UploadResponse;

public class UploadService {

  private static final String API_KEY = "6d207e02198a847aa98d0a2a901485a5";
  private static final String UPLOAD_URL = "https://freeimage.host/api/1/upload";

  private static String convertToBase64(MultipartFile file) {
    try {
      byte[] fileBytes = file.getBytes();
      return Base64.getEncoder().encodeToString(fileBytes);
    } catch (Exception e) {
      throw new RuntimeException("Failed to read file content", e);
    }
  }

  public static UploadResponse uploadImage(MultipartFile file) {
    try {
      String base64Image = convertToBase64(file);

      // Prepare request body
      MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
      body.add("key", API_KEY);
      body.add("action", "upload");
      body.add("source", base64Image);
      body.add("format", "json");

      // Prepare headers
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

      HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

      // Send POST request
      RestTemplate restTemplate = new RestTemplate();
      ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
          UPLOAD_URL,
          HttpMethod.POST,
          requestEntity,
          new ParameterizedTypeReference<Map<String, Object>>() {
          });

      // Extract image URL from the response
      if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
        Map<String, Object> responseBody = response.getBody();
        System.out.println(responseBody);
        Map<String, Object> image = (Map<String, Object>) responseBody.get("image");

        if (image == null) {
          throw new RuntimeException("No 'image' field found in response");
        }

        String displayUrl = (String) image.get("display_url");
        if (displayUrl == null) {
          throw new RuntimeException("No 'display_url' field in 'image' object");
        }

        UploadResponse uploadResponse = UploadResponse.builder()
            .imageUrl(displayUrl)
            .build();

        return uploadResponse;
      } else {
        throw new RuntimeException("Image upload failed: " + response.getStatusCode());
      }

    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
