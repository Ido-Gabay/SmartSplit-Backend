package com.project.SmartSplit.service;

import com.project.SmartSplit.dto.AuthenticationRequest;
import com.project.SmartSplit.dto.AuthenticationResponse;
import com.project.SmartSplit.entity.RefreshToken;
import com.project.SmartSplit.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlacklistService tokenBlacklistService;
    private final RefreshTokenService refreshTokenService;

    // The authenticate() method takes in an AuthenticationRequest object, which contains the username and password.
    // The method returns an AuthenticationResponse object, which contains the JWT and refresh token, and the user's roles.
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        // load the user details from the database using the username by calling the loadUserByUsername() method
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(authenticationRequest.getEmail());

        // check if the password matches the password in the database
        if (!authenticationRequest.getPassword().equals(userDetails.getPassword())) {
            throw new AuthenticationServiceException("Invalid credentials");
        }

        // generate the JWT token
        String jwtToken = jwtUtil.generateToken(authenticationRequest, userDetails);

        // check if the token is already blacklisted
        if (tokenBlacklistService.isBlacklisted(jwtToken)) {
            throw new AuthenticationServiceException("Invalid credentials");
        }

        // create a refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
        // get the user's roles
        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();

        // return the AuthenticationResponse object
        return new AuthenticationResponse(jwtToken, refreshToken.getToken(), roles);
    }
}


