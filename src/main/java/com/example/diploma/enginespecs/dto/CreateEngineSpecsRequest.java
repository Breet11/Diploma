package com.example.diploma.enginespecs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record CreateEngineSpecsRequest(
        @NotNull UUID engineTypeUuid,
        @NotBlank String fuelConsumption,
        @NotNull @Positive Long horsepower,
        @NotNull @Positive Long torque
) {
}

