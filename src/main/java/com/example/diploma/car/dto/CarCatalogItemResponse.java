package com.example.diploma.car.dto;

import java.util.UUID;

public record CarCatalogItemResponse(
        UUID uuid,
        UUID engineUuid,
        UUID carSpecsUuid,
        String brand,
        String model,
        Long releaseYear,
        Long topSpeed,
        String acceleration,
        String engineType,
        Long hourlyRentalPrice,
        String imageBase64,
        String imageContentType,
        boolean available
) {
}

