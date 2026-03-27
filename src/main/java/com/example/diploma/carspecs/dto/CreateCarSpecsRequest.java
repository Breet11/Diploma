package com.example.diploma.carspecs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record CreateCarSpecsRequest(
        @NotNull UUID carBrandUuid,
        @NotNull UUID carModelUuid,
        @NotBlank String acceleration,
        @NotNull @Positive Long topSpeed,
        @NotNull @Positive Long releaseYear
) {
}

