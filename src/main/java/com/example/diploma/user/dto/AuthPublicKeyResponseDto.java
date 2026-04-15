package com.example.diploma.user.dto;

public record AuthPublicKeyResponseDto(
        String algorithm,
        String publicKey
) {
}

