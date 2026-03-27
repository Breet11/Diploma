package com.example.diploma.carbrand.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCarBrandRequest(@NotBlank String name) {
}

