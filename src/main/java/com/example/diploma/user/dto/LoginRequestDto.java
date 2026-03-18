package com.example.diploma.user.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequestDto(@NotNull String login, @NotNull String hashedPassword) {
}
