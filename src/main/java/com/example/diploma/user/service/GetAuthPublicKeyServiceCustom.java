package com.example.diploma.user.service;

import com.example.diploma.user.dto.AuthPublicKeyResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetAuthPublicKeyServiceCustom implements GetAuthPublicKeyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetAuthPublicKeyServiceCustom.class);
    private static final String RSA_OAEP_SHA_256 = "RSA-OAEP-256";

    @Override
    public AuthPublicKeyResponseDto getPublicKey() {
        LOGGER.info("Static frontend RSA public key flow is enabled; backend public key endpoint is not used");
        return new AuthPublicKeyResponseDto(RSA_OAEP_SHA_256, "");
    }
}

