package com.abid.ocr.dto;

import lombok.Builder;

@Builder
public class ExtractTextRequest {
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
