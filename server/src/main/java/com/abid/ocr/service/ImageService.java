package com.abid.ocr.service;

import org.springframework.stereotype.Service;

@Service
public class ImageService {

    // static {
    // // System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    // System.load("/lib/java/libopencv_java490.so");
    // }

    // public BufferedImage preprocessImage(MultipartFile file) throws Exception {
    // // Convert MultipartFile to BufferedImage
    // byte[] bytes = file.getBytes();
    // BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(bytes));

    // // Convert BufferedImage to OpenCV Mat
    // Mat src = bufferedImageToMat(originalImage);

    // // Preprocessing pipeline
    // Mat processed = new Mat();
    // Imgproc.cvtColor(src, processed, Imgproc.COLOR_BGR2GRAY);
    // Imgproc.GaussianBlur(processed, processed, new Size(5, 5), 0);
    // Imgproc.threshold(processed, processed, 0, 255, Imgproc.THRESH_BINARY +
    // Imgproc.THRESH_OTSU);

    // return matToBufferedImage(processed);
    // }

    // private Mat bufferedImageToMat(BufferedImage bi) {
    // Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
    // byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
    // mat.put(0, 0, data);
    // return mat;
    // }

    // private BufferedImage matToBufferedImage(Mat mat) {
    // int type = BufferedImage.TYPE_BYTE_GRAY;
    // if (mat.channels() > 1) {
    // type = BufferedImage.TYPE_3BYTE_BGR;
    // }
    // BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
    // mat.get(0, 0, ((DataBufferByte)
    // image.getRaster().getDataBuffer()).getData());
    // return image;
    // }

}
