package com.abid.ocr.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageRequest {
    private String imageUrl;

    public ImageRequest(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}