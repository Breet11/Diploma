package com.example.diploma.engine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateEngineRequest(
		@NotBlank String engineName,
		@NotNull UUID engineTypeUuid,
		@NotBlank String fuelConsumption,
		@NotNull @Positive Long horsepower,
		@NotNull @Positive Long torque,
		@NotNull @Positive BigDecimal engineVolume
) {
}

