package com.example.diploma.rental.model;

import java.util.Arrays;
import java.util.Locale;

public enum RentalOrderStatus {
    NEW,
    IN_PROGRESS,
    APPROVED,
    REJECTED,
    COMPLETED;

    public static RentalOrderStatus fromValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Rental order status is required");
        }

        String normalized = value.trim().toUpperCase(Locale.ROOT);
        return Arrays.stream(values())
                .filter(status -> status.name().equals(normalized))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported rental order status: " + value));
    }
}

