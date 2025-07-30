package com.abid.ocr.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceiptDto {
  private String id;
  private List<Item> items;
  private Double subtotal;
  private Double tax;
  private Double total;
  private String currencyCode;
  private LocalDate date;

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("ReceiptResponse:\n");
    if (items != null && !items.isEmpty()) {
      sb.append("  Items:\n");
      for (Item item : items) {
        sb.append("    - ").append(item.getName())
            .append(" x").append(item.getQuantity())
            .append(" @ $").append(String.format("%.2f", item.getPrice()))
            .append("\n");
      }
    } else {
      sb.append("  Items: none\n");
    }
    sb.append("  Subtotal: $").append(String.format("%.2f", subtotal)).append("\n");
    sb.append("  Tax: $").append(String.format("%.2f", tax)).append("\n");
    sb.append("  Total: $").append(String.format("%.2f", total)).append("\n");
    return sb.toString();
  }

}
