package com.example.diploma.rental.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record RentalOrderProfileListItemResponse(
        UUID uuid,
        String car,
        Long hours,
        Long totalPrice,
        String status,
        OffsetDateTime createdAt
) {
}

