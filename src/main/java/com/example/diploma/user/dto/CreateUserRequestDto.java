package com.example.diploma.user.dto;

import com.example.diploma.user.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequestDto(
        @Email @NotBlank String email,
        @NotBlank String login,
        @NotBlank String password,
        Role role
) {
}

