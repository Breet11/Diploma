package com.example.diploma.utils.imageseed;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Locale;

@Service
public class ImageCompressionService {

    private final int maxWidth;
    private final int maxHeight;
    private final float jpegQuality;

    public ImageCompressionService(
            @Value("${app.seed.images.max-width:1280}") int maxWidth,
            @Value("${app.seed.images.max-height:720}") int maxHeight,
            @Value("${app.seed.images.jpeg-quality:0.82}") float jpegQuality
    ) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.jpegQuality = jpegQuality;
    }

    public CompressedImage compress(byte[] source, String detectedContentType) {
        if (source == null || source.length == 0) {
            throw new IllegalArgumentException("Source image is empty");
        }

        String contentType = normalizeContentType(detectedContentType);
        if (contentType.equals("image/svg+xml") || isSvgByContent(source)) {
            return new CompressedImage(minifySvg(source), "image/svg+xml");
        }

        return compressRaster(source, contentType);
    }

    private CompressedImage compressRaster(byte[] source, String contentType) {
        try {
            BufferedImage original = ImageIO.read(new java.io.ByteArrayInputStream(source));
            if (original == null) {
                throw new IllegalArgumentException("Unsupported image format");
            }

            BufferedImage resized = resizeIfNeeded(original);
            String outputFormat = pickOutputFormat(contentType, resized.getColorModel().hasAlpha());
            String outputContentType = outputFormat.equals("png") ? "image/png" : "image/jpeg";

            byte[] output = writeImage(resized, outputFormat);
            return new CompressedImage(output, outputContentType);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Failed to compress image", exception);
        }
    }

    private BufferedImage resizeIfNeeded(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();

        if (width <= maxWidth && height <= maxHeight) {
            return original;
        }

        double scale = Math.min((double) maxWidth / width, (double) maxHeight / height);
        int newWidth = Math.max(1, (int) Math.round(width * scale));
        int newHeight = Math.max(1, (int) Math.round(height * scale));

        int imageType = original.getColorModel().hasAlpha() ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage resized = new BufferedImage(newWidth, newHeight, imageType);

        Graphics2D graphics = resized.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.drawImage(original, 0, 0, newWidth, newHeight, null);
        graphics.dispose();

        return resized;
    }

    private byte[] writeImage(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        if (format.equals("jpg")) {
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            if (!writers.hasNext()) {
                throw new IllegalStateException("JPEG writer is not available");
            }

            ImageWriter writer = writers.next();
            ImageWriteParam writeParam = writer.getDefaultWriteParam();
            if (writeParam.canWriteCompressed()) {
                writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                writeParam.setCompressionQuality(jpegQuality);
            }

            try (ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream)) {
                writer.setOutput(imageOutputStream);
                writer.write(null, new IIOImage(image, null, null), writeParam);
            } finally {
                writer.dispose();
            }

            return outputStream.toByteArray();
        }

        ImageIO.write(image, format, outputStream);
        return outputStream.toByteArray();
    }

    private String pickOutputFormat(String contentType, boolean hasAlpha) {
        if ("image/png".equals(contentType) && hasAlpha) {
            return "png";
        }

        return "jpg";
    }

    private String normalizeContentType(String contentType) {
        if (contentType == null || contentType.isBlank()) {
            return "image/jpeg";
        }

        String normalized = contentType.trim().toLowerCase(Locale.ROOT);
        if (normalized.contains("svg")) {
            return "image/svg+xml";
        }

        if (normalized.contains("png")) {
            return "image/png";
        }

        if (normalized.contains("jpeg") || normalized.contains("jpg")) {
            return "image/jpeg";
        }

        return "image/jpeg";
    }

    private boolean isSvgByContent(byte[] source) {
        String text = new String(source, StandardCharsets.UTF_8).toLowerCase(Locale.ROOT);
        return text.contains("<svg");
    }

    private byte[] minifySvg(byte[] source) {
        String svg = new String(source, StandardCharsets.UTF_8);
        String minified = svg
                .replaceAll("<!--.*?-->", "")
                .replaceAll(">\\s+<", "><")
                .replaceAll("\\s{2,}", " ")
                .trim();

        return minified.getBytes(StandardCharsets.UTF_8);
    }

    public record CompressedImage(byte[] content, String contentType) {
    }
}

