package com.example.diploma.car.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record CreateCarRequest(
        @NotNull UUID engineUuid,
        @NotNull UUID carSpecsUuid,
        @NotNull @PositiveOrZero Long price,
        @NotBlank String imageUrl,
        boolean available
) {
}
