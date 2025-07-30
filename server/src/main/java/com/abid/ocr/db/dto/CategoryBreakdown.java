package com.abid.ocr.db.dto;

public class CategoryBreakdown {
    private String category;
    private Float amount;
    private Float percentage;

    public CategoryBreakdown() {
    }

    public CategoryBreakdown(String category, Float amount, Float percentage) {
        this.category = category;
        this.amount = amount;
        this.percentage = percentage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Float getPercentage() {
        return percentage;
    }

    public void setPercentage(Float percentage) {
        this.percentage = percentage;
    }
}
