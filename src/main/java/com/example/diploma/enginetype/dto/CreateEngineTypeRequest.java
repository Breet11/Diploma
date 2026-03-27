package com.example.diploma.enginetype.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateEngineTypeRequest(@NotBlank String engineType) {
}

