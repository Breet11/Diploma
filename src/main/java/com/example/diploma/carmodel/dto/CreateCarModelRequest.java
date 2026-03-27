package com.example.diploma.carmodel.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCarModelRequest(@NotBlank String name) {
}

