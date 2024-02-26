package com.project.SmartSplit.util;

import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@Component
// TODO 3: RSA KeyGenerator class
public class RSAKeyGenerator {
    /*
    Proper RSA key usage in JWT token generation and verification:
    Generating them in application, we should do so in a component or configuration class that runs at startup
    and injects them into JwtUtil.
     */

    public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }
}
