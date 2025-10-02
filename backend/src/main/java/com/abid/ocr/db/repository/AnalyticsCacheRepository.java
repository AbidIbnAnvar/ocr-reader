package com.abid.ocr.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abid.ocr.db.models.AnalyticsCache;
import com.abid.ocr.db.models.AnalyticsCacheId;

public interface AnalyticsCacheRepository extends JpaRepository<AnalyticsCache, AnalyticsCacheId> {
}
