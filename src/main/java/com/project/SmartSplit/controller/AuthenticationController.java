package com.project.SmartSplit.controller;

import com.project.SmartSplit.dto.AuthenticationRequest;
import com.project.SmartSplit.dto.AuthenticationResponse;
import com.project.SmartSplit.dto.RefreshTokenRequest;
import com.project.SmartSplit.exception.TokenRefreshException;
import com.project.SmartSplit.service.AuthenticationService;
import com.project.SmartSplit.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;

    // test
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("test");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) {
        try {

            // authenticate() method throws an AuthenticationServiceException if the username or password is invalid.
            AuthenticationResponse authResponse = authenticationService.authenticate(authenticationRequest);

            // print out the tokens, roles, and username, for testing
            System.out.println("refresh token: " + authResponse.getRefreshToken());
            System.out.println("jwt token: " + authResponse.getAccessToken());
            System.out.println("roles: " + authResponse.getRoles());
            System.out.println("username: " + authenticationRequest.getEmail());

            // if the username and password are valid, the method returns an AuthenticationResponse object OK response.
            return ResponseEntity.ok(authResponse);
        } catch (AuthenticationServiceException e) {
            // if the username or password is invalid, the method returns a 401 Unauthorized response.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            AuthenticationResponse authResponse = refreshTokenService.refresh(refreshTokenRequest.getRefreshToken());
            System.out.println("refresh token: " + authResponse.getRefreshToken());
            System.out.println("jwt token: " + authResponse.getAccessToken());
            return ResponseEntity.ok(authResponse);
        } catch (TokenRefreshException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
