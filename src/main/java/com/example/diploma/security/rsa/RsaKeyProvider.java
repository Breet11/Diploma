package com.example.diploma.security.rsa;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Component
public class RsaKeyProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(RsaKeyProvider.class);
    private static final String RSA_ALGORITHM = "RSA";
    private static final String PRIVATE_KEY_HEADER = "-----BEGIN PRIVATE KEY-----";
    private static final String PRIVATE_KEY_FOOTER = "-----END PRIVATE KEY-----";

    @Value("${security.rsa.private-key}")
    private String privateKeyPem;

    private PrivateKey privateKey;

    @PostConstruct
    public void initializePrivateKey() {
        try {
            if (privateKeyPem == null || privateKeyPem.isBlank() || privateKeyPem.contains("PASTE_YOUR_PRIVATE_RSA_KEY_HERE")) {
                throw new IllegalStateException("Property security.rsa.private-key must contain a PKCS#8 PEM private key");
            }
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            byte[] keyBytes = Base64.getDecoder().decode(normalizePem(privateKeyPem));
            privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
            LOGGER.info("RSA private key loaded successfully from application properties");
        } catch (GeneralSecurityException | IllegalArgumentException exception) {
            throw new IllegalStateException("Failed to initialize RSA private key", exception);
        }
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    private String normalizePem(String pem) {
        return pem
                .replace("\\n", "")
                .replace(PRIVATE_KEY_HEADER, "")
                .replace(PRIVATE_KEY_FOOTER, "")
                .replaceAll("\\s", "");
    }
}

