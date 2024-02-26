package com.project.SmartSplit.config;

import com.project.SmartSplit.util.RSAKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

@Configuration
public class RSAConfiguration {

    @Bean
    public KeyPair rsaKeyPair() {
        try {
            RSAKeyGenerator rsaKeyGenerator = new RSAKeyGenerator();
            return rsaKeyGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Failed to generate RSA key pair", e);
        }
    }
}