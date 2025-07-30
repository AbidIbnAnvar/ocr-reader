package com.abid.ocr.service;

import java.awt.image.BufferedImage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
public class OcrService {
    @Value("${tess4j.datapath}")
    private String tessDataPath;

    public String extractText(BufferedImage image) throws TesseractException {
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath(tessDataPath);
        tesseract.setLanguage("eng");
        tesseract.setPageSegMode(1);
        tesseract.setOcrEngineMode(1);
        return tesseract.doOCR(image);
    }
}
