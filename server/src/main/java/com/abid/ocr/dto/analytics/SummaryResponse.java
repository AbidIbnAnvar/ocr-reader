package com.abid.ocr.dto.analytics;

import java.util.List;

import com.abid.ocr.db.dto.CategoryBreakdown;

public class SummaryResponse {
    private Double totalSpending;
    private Long receiptCount;
    private Integer categoryCount;
    private Double monthlyAverage;
    private List<CategoryBreakdown> categoryBreakdown;

    public SummaryResponse() {
    }

    public SummaryResponse(
            Double totalSpending,
            Long receiptCount,
            Integer categoryCount,
            Double monthlyAverage,
            List<CategoryBreakdown> categoryBreakdown) {
        this.totalSpending = totalSpending;
        this.receiptCount = receiptCount;
        this.categoryCount = categoryCount;
        this.monthlyAverage = monthlyAverage;
        this.categoryBreakdown = categoryBreakdown;
    }

    public Long getReceiptCount() {
        return receiptCount;
    }

    public void setReceiptCount(Long receiptCount) {
        this.receiptCount = receiptCount;
    }

    public Double getTotalSpending() {
        return totalSpending;
    }

    public void setTotalSpending(Double totalSpending) {
        this.totalSpending = totalSpending;
    }

    public Double getMonthlyAverage() {
        return monthlyAverage;
    }

    public void setMonthlyAverage(Double monthlyAverage) {
        this.monthlyAverage = monthlyAverage;
    }

    public Integer getCategoryCount() {
        return categoryCount;
    }

    public void setCategoryCount(Integer categoryCount) {
        this.categoryCount = categoryCount;
    }

    public List<CategoryBreakdown> getCategoryBreakdown() {
        return categoryBreakdown;
    }

    public void setCategoryBreakdown(List<CategoryBreakdown> categoryBreakdown) {
        this.categoryBreakdown = categoryBreakdown;
    }
}
