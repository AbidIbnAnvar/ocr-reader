package com.abid.ocr.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.abid.ocr.db.services.ReceiptService;
import com.abid.ocr.dto.ErrorResponse;
import com.abid.ocr.dto.ExtractTextRequest;
import com.abid.ocr.dto.ImageRequest;
import com.abid.ocr.dto.ReceiptDto;
import com.abid.ocr.dto.UploadResponse;
import com.abid.ocr.exception.ReceiptProcessingException;
import com.abid.ocr.service.ReceiptProcessingService;
import com.abid.ocr.service.S3Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ReceiptController {
  private final ReceiptProcessingService receiptProcessingService;
  private final ReceiptService receiptService;
  private final S3Service s3Service;

  @PostMapping("/upload")
  public ResponseEntity<?> uploadReceipt(@RequestParam("file") MultipartFile file) {
    try {
      if (file.isEmpty()) {
        throw new ReceiptProcessingException("File is empty");
      }
      String contentType = file.getContentType();
      if (contentType == null || !contentType.startsWith("image/")) {
        throw new ReceiptProcessingException("Invalid file type. Please upload an image.");
      }
      // UploadResponse response = UploadService.uploadImage(file);
      UploadResponse response = s3Service.uploadFile(file);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ErrorResponse("Error uploading receipt: " + e.getMessage()));
    }
  }

  @PostMapping("/extract-text")
  public ResponseEntity<?> processReceipt(@RequestBody ExtractTextRequest request) {
    try {
      String image = request.getImageUrl();
      ReceiptDto response = receiptProcessingService.processReceipt(new ImageRequest(image));
      return ResponseEntity.ok(response);
    } catch (ReceiptProcessingException e) {
      return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ErrorResponse("Error processing receipt"));
    }
  }

  @PostMapping("/receipts")
  public ResponseEntity<?> saveReceipt(@RequestBody ReceiptDto request) {
    try {
      return ResponseEntity.ok(receiptService.createReceipt(request));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ErrorResponse("Error saving receipt"));
    }
  }

  @GetMapping("/receipts")
  public ResponseEntity<?> getReceipts() {
    try {
      return ResponseEntity.ok(receiptService.getAllReceipts());
    } catch (Exception e) {
      throw new RuntimeException("Unable to fetch receipts");
    }
  }

  @PutMapping("/receipts/{id}")
  public ResponseEntity<?> updateReceipt(@PathVariable String id, @RequestBody ReceiptDto request) {
    try {
      System.out.println(id);
      UUID receiptId = UUID.fromString(id);
      return ResponseEntity.ok(receiptService.updateReceipt(receiptId, request));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ErrorResponse("Error updating receipt"));
    }
  }

  @DeleteMapping("/receipts/{id}")
  public ResponseEntity<?> deleteReceipt(@PathVariable String id) {
    try {
      return ResponseEntity.ok(receiptService.deleteReceipt(UUID.fromString(id)));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ErrorResponse("Error updating receipt"));

    }
  }

}
