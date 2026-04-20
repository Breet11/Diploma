package com.example.diploma.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDto(
        @Email @NotBlank String email,
        @NotBlank String login,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String phone,
        @NotBlank String password,
        @NotBlank String confirmPassword
) {
}
