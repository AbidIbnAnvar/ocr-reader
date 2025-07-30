package com.abid.ocr.db.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abid.ocr.db.models.UserSettings;

public interface UserSettingsRepository extends JpaRepository<UserSettings, UUID> {
}
