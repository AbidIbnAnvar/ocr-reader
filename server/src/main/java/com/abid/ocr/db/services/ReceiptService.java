package com.abid.ocr.db.services;

import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.abid.ocr.db.dto.CategoryBreakdown;
import com.abid.ocr.db.models.Receipt;
import com.abid.ocr.db.models.ReceiptItem;
import com.abid.ocr.db.models.User;
import com.abid.ocr.db.repository.ReceiptRepository;
import com.abid.ocr.db.repository.UserRepository;
import com.abid.ocr.dto.ReceiptDto;
import com.abid.ocr.service.JwtService;

@Service
public class ReceiptService {
  @Autowired
  private ReceiptRepository receiptRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtService jwtService;

  public List<Receipt> getAllReceipts() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    UUID userId = userRepository.getUserIdByEmail(email);
    return receiptRepository.findAllByUserId(userId);
  }

  public Receipt createReceipt(ReceiptDto dto) {
    Receipt receipt = new Receipt();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    User user = userRepository
        .findByEmail(email)
        .orElseThrow(() -> new RuntimeException("No user found"));
    receipt.setCurrencyCode(dto.getCurrencyCode());
    receipt.setSubtotal(dto.getSubtotal());
    receipt.setTax(dto.getTax());
    receipt.setTotal(dto.getTotal());
    receipt.setDate(dto.getDate()); // or use dto.getDate() if available
    receipt.setUser(user); // however you retrieve current user

    // Convert DTO items to ReceiptItems
    List<ReceiptItem> items = dto.getItems().stream()
        .map(item -> {
          ReceiptItem ri = new ReceiptItem();
          ri.setName(item.getName());
          ri.setQuantity(item.getQuantity());
          ri.setPrice(item.getPrice());
          ri.setReceipt(receipt); // link back
          return ri;
        })
        .collect(Collectors.toList());

    receipt.setItems(items);
    return receiptRepository.save(receipt);
  }

  public Receipt updateReceipt(UUID id, ReceiptDto dto) {
    Receipt existingReceipt = receiptRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("No Receipt Found"));

    existingReceipt.setCurrencyCode(dto.getCurrencyCode());
    existingReceipt.setSubtotal(dto.getSubtotal());
    existingReceipt.setTax(dto.getTax());
    existingReceipt.setTotal(dto.getTotal());
    existingReceipt.setDate(dto.getDate());
    List<ReceiptItem> items = dto.getItems().stream()
        .map(item -> {
          ReceiptItem ri = new ReceiptItem();
          ri.setName(item.getName());
          ri.setQuantity(item.getQuantity());
          ri.setPrice(item.getPrice());
          ri.setReceipt(existingReceipt); // link back
          return ri;
        })
        .collect(Collectors.toList());
    existingReceipt.getItems().clear();
    existingReceipt.getItems().addAll(items);
    return receiptRepository.save(existingReceipt);
  }

  public Receipt deleteReceipt(UUID id) {
    Receipt existingReceipt = receiptRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("No Receipt Found"));

    receiptRepository.deleteById(id);
    return existingReceipt;
  }

  public Double getTotalSpending(UUID userId) {
    Double total = receiptRepository.getTotalSpendingByUserId(userId);
    return total != null ? total : 0.0;
  }

  public long getReceiptCount(UUID userId) {
    return receiptRepository.getReceiptCountByUserId(userId);
  }

  public List<CategoryBreakdown> getCategoryBreakdown(UUID userId) {
    List<CategoryBreakdown> breakdown = receiptRepository.getCategoryBreakdownByUserId(userId);

    // Optional: calculate percentages
    Float total = breakdown.stream()
        .map(CategoryBreakdown::getAmount)
        .filter(Objects::nonNull)
        .reduce(0f, Float::sum);

    for (CategoryBreakdown b : breakdown) {
      float percent = (total > 0) ? (b.getAmount() / total) * 100 : 0;
      b.setPercentage(percent);
    }
    return breakdown;
  }

  public Double getMonthlyAverage(UUID userId) {
    List<Receipt> receipts = receiptRepository.findAllByUserId(userId);

    if (receipts.isEmpty())
      return 0.0;

    double totalSpending = receipts.stream()
        .filter(r -> r.getTotal() != null)
        .mapToDouble(Receipt::getTotal)
        .sum();

    long distinctMonths = receipts.stream()
        .map(Receipt::getDate)
        .filter(Objects::nonNull)
        .map(date -> YearMonth.from(date))
        .distinct()
        .count();

    return distinctMonths == 0 ? 0.0 : totalSpending / distinctMonths;

  }

}
