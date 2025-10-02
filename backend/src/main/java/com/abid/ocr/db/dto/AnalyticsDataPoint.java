package com.abid.ocr.db.dto;

public class AnalyticsDataPoint {
    private String label; // e.g., "2025", "Sep 2025", "2025-09-02"
    private Double amount;

    public AnalyticsDataPoint(String label, Double amount) {
        this.label = label;
        this.amount = amount;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
