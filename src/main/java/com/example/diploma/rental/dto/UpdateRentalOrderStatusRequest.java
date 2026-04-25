package com.example.diploma.rental.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateRentalOrderStatusRequest(
        @NotBlank String status
) {
}

