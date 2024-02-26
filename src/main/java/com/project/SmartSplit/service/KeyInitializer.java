package com.project.SmartSplit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import com.project.SmartSplit.repository.RSAKeysRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeyInitializer implements InitializingBean {

    private final KeyRotationService keyRotationService;
    private final RSAKeysRepository RSAKeysRepository;



    @Override
    public void afterPropertiesSet() {
        // Check if the database already has keys. If not, generate and store them.
        if (RSAKeysRepository.count() == 0) {
            keyRotationService.rotateKeys();
        }
    }
}