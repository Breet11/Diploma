package com.example.diploma.rental.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record RentalOrderAdminListItemResponse(
        UUID uuid,
        String customer,
        String phone,
        String car,
        Long hours,
        Long totalPrice,
        String status,
        OffsetDateTime createdAt
) {
}

