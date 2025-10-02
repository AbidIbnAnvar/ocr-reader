package com.abid.ocr;

import org.opencv.core.Core;

public class OpenCVTest {
    static {
        System.load("/lib/java/libopencv_java490.so");
    }

    public static void main(String[] args) {
        System.out.println("OpenCV loaded successfully!");
        System.out.println("Version: " + Core.VERSION);
    }
}
