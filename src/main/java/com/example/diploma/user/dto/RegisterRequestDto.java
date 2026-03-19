package com.example.diploma.user.dto;

import jakarta.validation.constraints.NotNull;

public record RegisterRequestDto(
        @NotNull String email,
        @NotNull String login,
        @NotNull String password
) {
}
