package com.abid.ocr.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.abid.ocr.config.OpenRouterConfig;
import com.abid.ocr.dto.ImageRequest;
import com.abid.ocr.dto.ReceiptDto;
import com.abid.ocr.exception.ReceiptProcessingException;

@Service
public class ReceiptProcessingService {

  private static final Logger logger = LoggerFactory.getLogger(ReceiptProcessingService.class);
  @Value("${openrouter.api-key}")
  private String apiKey;

  public ReceiptDto processReceipt(ImageRequest request) throws ReceiptProcessingException {
    try {
      String imageUrl = request.getImageUrl();
      OpenRouterConfig config = new OpenRouterConfig();
      if (apiKey == null || apiKey.isEmpty()) {
        throw new RuntimeException("API key is required!");
      }
      config.setApiKey(apiKey);
      config.setModel("moonshotai/kimi-vl-a3b-thinking:free");
      config.setApiUrl("https://openrouter.ai/api/v1/chat/completions");

      // 2) Instantiate the service with that config
      OpenRouterService service = new OpenRouterService(config);

      String prompt = """
          You are an intelligent OCR parser. Analyze the receipt image and output only this JSON structure:

          {
            "items": [{"name": "...", "price": ..., "quantity": ...}],
            "subtotal": ...,
            "tax": ...,
            "total": ...,
            "currencyCode": ...,
          }

          ❗ Important:
          - Only return valid JSON — no markdown, no explanation, no comments.
          - Do not include any introduction or reasoning.
          - Do not wrap the JSON in triple backticks.
          - Give 3 letter currency code (eg: USD,INR,...)
          """;
      String apiResponse = service.sendImageQuery(imageUrl, prompt);
      // System.out.println("API Response:" + apiResponse);
      String cleanedResponse = JsonService.extractCleanJson(apiResponse);
      // System.out.println(cleanedResponse);
      ReceiptDto response = JsonService.parseReceipt(cleanedResponse);
      return response;
    } catch (Exception e) {
      logger.error("Receipt processing error: {}", e.getMessage());
      throw new ReceiptProcessingException(e.getMessage());
    }
  }

}
