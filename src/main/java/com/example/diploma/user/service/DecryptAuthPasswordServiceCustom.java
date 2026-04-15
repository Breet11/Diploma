package com.example.diploma.user.service;

import com.example.diploma.security.rsa.RsaKeyProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.spec.MGF1ParameterSpec;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class DecryptAuthPasswordServiceCustom implements DecryptAuthPasswordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DecryptAuthPasswordServiceCustom.class);
    private static final String RSA_TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";

    private final RsaKeyProvider rsaKeyProvider;

    @Override
    public String decrypt(String encryptedPassword) {
        LOGGER.info("Decrypting RSA-protected password payload");
        try {
            Cipher cipher = Cipher.getInstance(RSA_TRANSFORMATION);
            cipher.init(
                    Cipher.DECRYPT_MODE,
                    rsaKeyProvider.getPrivateKey(),
                    new OAEPParameterSpec(
                            "SHA-256",
                            "MGF1",
                            MGF1ParameterSpec.SHA256,
                            PSource.PSpecified.DEFAULT
                    )
            );
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (GeneralSecurityException | IllegalArgumentException exception) {
            throw new IllegalArgumentException("Password decryption failed", exception);
        }
    }
}

