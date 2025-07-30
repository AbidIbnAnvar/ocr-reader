package com.abid.ocr.db.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "receipt_items")
public class ReceiptItem {
  @Id
  @GeneratedValue
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "receipt_id", nullable = false)
  @JsonBackReference
  private Receipt receipt;

  private String name;
  private Double price;
  private int quantity = 1;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  @Column(name = "created_at")
  private LocalDateTime createdAt = LocalDateTime.now();

  // Getters and Setters

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Receipt getReceipt() {
    return receipt;
  }

  public void setReceipt(Receipt receipt) {
    this.receipt = receipt;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
