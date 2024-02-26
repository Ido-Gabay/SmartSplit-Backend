package com.project.SmartSplit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String accessToken;
    private List<String> roles;
    private String refreshToken;

    public AuthenticationResponse(String accessToken, String refreshToken,
                                  // pay attention: the roles are a collection of GrantedAuthority objects
                                  Collection<? extends GrantedAuthority> roles) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        // convert the roles to a list of strings, for the response body, because the response body is JSON
        this.roles = roles.stream().map(GrantedAuthority::getAuthority).toList();
    }
}

