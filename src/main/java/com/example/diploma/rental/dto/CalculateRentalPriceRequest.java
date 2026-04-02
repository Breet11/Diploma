package com.example.diploma.rental.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record CalculateRentalPriceRequest(
        @NotNull UUID carUuid,
        @NotNull @Positive Long hours
) {
}

