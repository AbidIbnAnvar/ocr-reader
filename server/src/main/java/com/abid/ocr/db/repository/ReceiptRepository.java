package com.abid.ocr.db.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.abid.ocr.db.dto.CategoryBreakdown;
import com.abid.ocr.db.models.Receipt;

public interface ReceiptRepository extends JpaRepository<Receipt, UUID> {

  List<Receipt> findAllByUserId(UUID userId);

  @Query("SELECT r FROM Receipt r WHERE r.user.email = :email")
  List<Receipt> findAllByUserEmail(@Param("email") String email);

  @Query("SELECT SUM(r.total) FROM Receipt r WHERE r.user.id = :userId")
  Double getTotalSpendingByUserId(@Param("userId") UUID userId);

  @Query("SELECT SUM(r.total) FROM Receipt r WHERE r.user.email = :email")
  Double getTotalSpendingByEmail(@Param("email") String email);

  @Query("SELECT COUNT(r) FROM Receipt r WHERE r.user.id = :userId")
  long getReceiptCountByUserId(@Param("userId") UUID userId);

  @Query(value = """
      SELECT
          c.name AS category,
          SUM(ri.price * ri.quantity) AS amount,
          0 AS percentage
      FROM receipt_items ri
      JOIN categories c ON ri.category_id = c.id
      WHERE ri.receipt_id IN (
          SELECT id FROM receipts WHERE user_id = :userId
      )
      GROUP BY c.name
      """, nativeQuery = true)
  List<CategoryBreakdown> getCategoryBreakdownByUserId(@Param("userId") UUID userId);

}
