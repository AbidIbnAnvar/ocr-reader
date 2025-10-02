package com.abid.ocr.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.abid.ocr.dto.UploadResponse;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3Service {
  @Value("${aws.bucketName}")
  private String bucketName;

  private final S3Client s3Client;

  public UploadResponse uploadFile(MultipartFile file) throws IOException {
    String key = UUID.randomUUID() + "_" + file.getOriginalFilename();

    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .contentType(file.getContentType())
        .build();

    s3Client.putObject(putObjectRequest,
        RequestBody.fromBytes(file.getBytes()));
    String imageUrl = getFileUrl(key);
    UploadResponse response = UploadResponse.builder()
        .imageUrl(imageUrl)
        .build();
    return response;
  }

  public String getFileUrl(String key) {
    return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
  }

}
