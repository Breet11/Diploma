package com.example.diploma.unit;

import com.example.diploma.utils.imageseed.ImageCompressionService;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImageCompressionUnitTest {

    private final ImageCompressionService imageCompressionService = new ImageCompressionService(640, 360, 0.8f);

    @Test
    void shouldMinifySvgAndKeepSvgContentType() {
        String svg = """
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 50">
                    <!-- comment -->
                    <rect width="100" height="50" fill="#111" />
                </svg>
                """;

        ImageCompressionService.CompressedImage compressed = imageCompressionService.compress(
                svg.getBytes(StandardCharsets.UTF_8),
                "image/svg+xml"
        );

        String minified = new String(compressed.content(), StandardCharsets.UTF_8);
        assertEquals("image/svg+xml", compressed.contentType());
        assertTrue(minified.contains("<svg"));
        assertTrue(minified.indexOf("<!--") < 0);
    }

    @Test
    void shouldResizeLargeRasterImage() throws Exception {
        BufferedImage image = new BufferedImage(2000, 1200, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.BLUE);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.dispose();

        ByteArrayOutputStream pngBytes = new ByteArrayOutputStream();
        ImageIO.write(image, "png", pngBytes);

        ImageCompressionService.CompressedImage compressed = imageCompressionService.compress(
                pngBytes.toByteArray(),
                "image/png"
        );

        BufferedImage restored = ImageIO.read(new java.io.ByteArrayInputStream(compressed.content()));
        assertEquals("image/jpeg", compressed.contentType());
        assertTrue(restored.getWidth() <= 640);
        assertTrue(restored.getHeight() <= 360);
    }
}

