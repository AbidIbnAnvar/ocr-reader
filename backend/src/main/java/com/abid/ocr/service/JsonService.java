package com.abid.ocr.service;

import com.abid.ocr.dto.ReceiptDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonService {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static ReceiptDto parseReceipt(String jsonString) {
    try {
      // Step 1: Parse top-level JSON
      JsonNode root = objectMapper.readTree(jsonString);

      // Step 2: Navigate to choices[0].message.content
      JsonNode contentNode = root.path("choices").get(0).path("message").path("content");

      if (contentNode.isMissingNode()) {
        throw new RuntimeException("Missing 'content' field in response.");
      }

      String contentJson = contentNode.asText(); // This is the JSON string of the receipt
      System.out.println(contentJson);
      // Step 3: Parse that content string into ReceiptResponse
      return objectMapper.readValue(contentJson, ReceiptDto.class);

    } catch (Exception e) {
      throw new RuntimeException("Failed to parse receipt response", e);
    }
  }

  public static String extractCleanJson(String response) {
    // 1. Remove ◁think▷...◁/think▷ block
    String withoutThink = response.replaceAll("(?s)◁think▷.*?◁/think▷", "").trim();

    // 2. Remove markdown code block markers (like ```json or ```)
    String cleaned = withoutThink.replaceAll("(?i)```json", "") // remove ```json (case-insensitive)
        .replaceAll("```", "") // remove ```
        .trim();

    // 3. Extract the first valid JSON block
    int start = cleaned.indexOf('{');
    int end = cleaned.lastIndexOf('}');
    if (start >= 0 && end > start) {
      return cleaned.substring(start, end + 1);
    }

    throw new RuntimeException("Could not extract JSON from response.");
  }

}
