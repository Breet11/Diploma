package com.example.diploma.rental.dto;

import java.util.UUID;

public record CreateRentalResponse(
        UUID rentalUuid,
        Long totalPrice,
        String message
) {
}

