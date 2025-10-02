package com.abid.ocr;

import com.abid.ocr.config.OpenRouterConfig;
import com.abid.ocr.dto.ReceiptDto;
import com.abid.ocr.service.JsonService;
import com.abid.ocr.service.OpenRouterService;

public class AITest {

  public static void main(String[] args) {
    // 1) Manually create & configure your config object
    OpenRouterConfig config = new OpenRouterConfig();
    // TODO:
    String apiKey = System.getenv("OPENROUTER_API_KEY");
    if (apiKey == null || apiKey.isEmpty()) {
      throw new RuntimeException("API key is required!");
    }
    config.setApiKey(apiKey);
    config.setModel("moonshotai/kimi-vl-a3b-thinking:free");
    config.setApiUrl("https://openrouter.ai/api/v1/chat/completions");

    // 2) Instantiate the service with that config
    OpenRouterService service = new OpenRouterService(config);

    // 3) Call the method you want to verify
    String imageUrl = "https://iili.io/Fal4e2e.jpg";
    String prompt = """
        You are an intelligent OCR parser. Analyze the receipt image and output only this JSON structure:

        {
          "items": [{"name": "...", "price": ..., "quantity": ...}],
          "subtotal": ...,
          "tax": ...,
          "total": ...
        }

        ❗ Important:
        - Only return valid JSON — no markdown, no explanation, no comments.
        - Do not include any introduction or reasoning.
        - Do not wrap the JSON in triple backticks.
        """;
    try {
      String apiResponse = service.sendImageQuery(imageUrl, prompt);
      System.out.println("API Response:");
      String cleanedResponse = JsonService.extractCleanJson(apiResponse);
      System.out.println(cleanedResponse);
      ReceiptDto response = JsonService.parseReceipt(cleanedResponse);

    } catch (Exception e) {
      System.err.println("Test failed: " + e.getMessage());
    }
  }
}
