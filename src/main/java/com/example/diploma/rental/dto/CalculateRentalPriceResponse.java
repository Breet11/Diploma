package com.example.diploma.rental.dto;

import java.math.BigDecimal;

public record CalculateRentalPriceResponse(
        Long baseHourlyPrice,
        Long hours,
        BigDecimal multiplier,
        Long totalPrice
) {
}

