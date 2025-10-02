package com.abid.ocr.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abid.ocr.db.dto.AnalyticsDataPoint;
import com.abid.ocr.db.dto.CategoryBreakdown;
import com.abid.ocr.db.services.ReceiptService;
import com.abid.ocr.db.services.UserService;
import com.abid.ocr.dto.ErrorResponse;
import com.abid.ocr.dto.analytics.SummaryResponse;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

  private final ReceiptService receiptService;
  private final UserService userService;

  public AnalyticsController(
      ReceiptService receiptService,
      UserService userService) {
    this.receiptService = receiptService;
    this.userService = userService;
  }

  @GetMapping("/summary")
  public ResponseEntity<?> getAnalyticsSummary() {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String email = authentication.getName();
      System.out.println(email);
      UUID userId = userService.getUserIdFromEmail(email);
      Double totalSpending = receiptService.getTotalSpending(userId);
      Long receiptCount = receiptService.getReceiptCount(userId);
      List<CategoryBreakdown> categoryBreakdown = receiptService.getCategoryBreakdown(userId);
      int categoryCount = categoryBreakdown.size();
      Double monthlyAverage = receiptService.getMonthlyAverage(userId);
      SummaryResponse response = new SummaryResponse(totalSpending, receiptCount, categoryCount, monthlyAverage,
          categoryBreakdown);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ErrorResponse("Error getting analytics summary:" + e.getMessage()));
    }
  }

  @GetMapping("/timeseries")
  public ResponseEntity<?> getAnalyticsTimeseries(
      @RequestParam String granularity,
      @RequestParam(defaultValue = "0") int offset) {
    try {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      String email = auth.getName();
      UUID userId = userService.getUserIdFromEmail(email);

      List<AnalyticsDataPoint> data = receiptService.getAnalytics(userId, granularity, offset);
      return ResponseEntity.ok(data);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ErrorResponse("Error fetching analytics timeseries: " + e.getMessage()));
    }
  }

}
