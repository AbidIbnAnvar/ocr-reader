package com.abid.ocr.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveReceiptRequest {
  private String id;
  private List<Item> items;
  private Double subtotal;
  private Double tax;
  private Double total;
  private String currencyCode;
}
