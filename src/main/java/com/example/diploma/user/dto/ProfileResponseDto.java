package com.example.diploma.user.dto;

public record ProfileResponseDto(
        String email,
        String login,
        String firstName,
        String lastName,
        String phone,
        String role
) {
}

