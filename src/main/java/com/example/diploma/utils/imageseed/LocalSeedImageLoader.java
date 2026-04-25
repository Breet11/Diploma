package com.example.diploma.utils.imageseed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocalSeedImageLoader implements ApplicationRunner {

    private static final List<String> SUPPORTED_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".webp", ".svg");

    private final JdbcTemplate jdbcTemplate;
    private final ImageCompressionService imageCompressionService;

    @Value("${app.seed.images.enabled:true}")
    private boolean enabled;

    @Value("${app.seed.images.dir:}")
    private String seedImagesDir;

    @Override
    public void run(ApplicationArguments args) {
        if (!enabled) {
            log.info("Local seed image loader is disabled");
            return;
        }

        if (seedImagesDir == null || seedImagesDir.isBlank()) {
            log.info("Local seed image loader skipped: app.seed.images.dir is empty");
            return;
        }

        Path root = Paths.get(seedImagesDir).toAbsolutePath().normalize();
        if (!Files.isDirectory(root)) {
            log.warn("Local seed image loader skipped: directory not found [{}]", root);
            return;
        }

        List<CarSeedImageTarget> cars = jdbcTemplate.query(
                """
                        SELECT uuid, image_url
                        FROM dip_car
                        WHERE image_url LIKE 'seed:%'
                        """,
                (resultSet, rowNum) -> new CarSeedImageTarget(
                        resultSet.getObject("uuid", UUID.class),
                        resultSet.getString("image_url")
                )
        );

        int updated = 0;
        for (CarSeedImageTarget car : cars) {
            String key = extractSeedKey(car.imageUrl());
            if (key == null) {
                continue;
            }

            Path imagePath = findImagePath(root, key);
            if (imagePath == null) {
                log.debug("No local image found for key [{}] in [{}]", key, root);
                continue;
            }

            try {
                byte[] source = Files.readAllBytes(imagePath);
                String contentType = detectContentType(imagePath);
                ImageCompressionService.CompressedImage compressed = imageCompressionService.compress(source, contentType);

                int rows = jdbcTemplate.update(
                        "UPDATE dip_car SET image_blob = ?, image_content_type = ? WHERE uuid = ?",
                        compressed.content(),
                        compressed.contentType(),
                        car.uuid()
                );

                if (rows > 0) {
                    updated += rows;
                }
            } catch (IOException exception) {
                log.warn("Failed to load image for key [{}] from [{}]: {}", key, imagePath, exception.getMessage());
            } catch (RuntimeException exception) {
                log.warn("Failed to process image for key [{}] from [{}]: {}", key, imagePath, exception.getMessage());
            }
        }

        log.info("Local seed image loader completed. Updated {} cars", updated);
    }

    private String extractSeedKey(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank() || !imageUrl.startsWith("seed:")) {
            return null;
        }

        return imageUrl.substring("seed:".length());
    }

    private Path findImagePath(Path root, String key) {
        for (String extension : SUPPORTED_EXTENSIONS) {
            Path candidate = root.resolve(key + extension);
            if (Files.isRegularFile(candidate)) {
                return candidate;
            }
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(root, key + ".*")) {
            for (Path candidate : stream) {
                if (Files.isRegularFile(candidate) && isSupported(candidate)) {
                    return candidate;
                }
            }
        } catch (IOException exception) {
            log.debug("Directory scan failed for [{}]: {}", key, exception.getMessage());
        }

        return null;
    }

    private boolean isSupported(Path file) {
        String name = file.getFileName().toString().toLowerCase(Locale.ROOT);
        return SUPPORTED_EXTENSIONS.stream().anyMatch(name::endsWith);
    }

    private String detectContentType(Path imagePath) {
        try {
            String type = Files.probeContentType(imagePath);
            if (type != null && !type.isBlank()) {
                return type;
            }
        } catch (IOException ignored) {
            // Fallback by extension if probe fails.
        }

        String name = imagePath.getFileName().toString().toLowerCase(Locale.ROOT);
        if (name.endsWith(".svg")) {
            return "image/svg+xml";
        }
        if (name.endsWith(".webp")) {
            return "image/webp";
        }
        if (name.endsWith(".png")) {
            return "image/png";
        }
        if (name.endsWith(".jpg") || name.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        return "image/jpeg";
    }

    private record CarSeedImageTarget(UUID uuid, String imageUrl) {
    }
}

