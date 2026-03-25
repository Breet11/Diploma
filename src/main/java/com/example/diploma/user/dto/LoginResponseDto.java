package com.example.diploma.user.dto;

public record LoginResponseDto(
		String accessToken,
		String tokenType,
		long expiresIn,
		String role
) {
}
