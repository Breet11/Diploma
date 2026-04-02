package com.example.diploma.loyalty.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateLoyaltyRuleRequest(
        @NotNull @Positive Long minHours,
        Long maxHours,
        @NotNull @DecimalMin(value = "0.0001") BigDecimal multiplier,
        boolean active
) {
}

