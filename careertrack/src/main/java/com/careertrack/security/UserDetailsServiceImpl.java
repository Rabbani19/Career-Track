package com.careertrack.security;

import com.careertrack.model.User;
import com.careertrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl
        implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        // ✅ Try by username first, then by email
        User user = userRepository
                .findByUsername(username)
                .orElseGet(() ->
                        userRepository
                                .findByEmail(username)
                                .orElseThrow(() ->
                                        new UsernameNotFoundException(
                                                "User not found: " + username
                                        )
                                )
                );

        return org.springframework.security.core
                .userdetails.User
                .builder()
                // ✅ Use getUsername() from User.java
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(
                        Collections.singletonList(
                                new SimpleGrantedAuthority(
                                        "ROLE_" + user.getRole().name()
                                )
                        )
                )
                .build();
    }
}