package com.abid.ocr.db.models;

import java.io.Serializable;
import java.util.UUID;

import com.abid.ocr.db.enums.CacheType;

public class AnalyticsCacheId implements Serializable {
    private UUID userId;
    private CacheType type;
    private String period;

    // equals(), hashCode(), default constructor

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public CacheType getType() {
        return type;
    }

    public void setType(CacheType type) {
        this.type = type;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
