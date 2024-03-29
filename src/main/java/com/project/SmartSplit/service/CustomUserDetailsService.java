package com.project.SmartSplit.service;

import com.project.SmartSplit.entity.Role;
import com.project.SmartSplit.entity.User;
import com.project.SmartSplit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Fetch the user from the database. If the user is not found, throw an exception.
        User user = userRepository.findByEmail(email);

        //
        if (user != null) {
            // Create a UserDetails object from the data fetched from the database.
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    mapRolesToAuthorities(user.getRoles())

            );
        } else {
            // throw new UsernameNotFoundException("Invalid username or password.");
            System.out.println("Invalid username or password, or logout out.");
            return null;
        }
    }

    // This method converts the user's roles to a list of GrantedAuthority objects, which can be used for role based authentication and authorization.
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
