package com.example.diploma.user.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(@NotBlank String login, @NotBlank String password) {
}
