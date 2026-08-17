package com.careertrack.config;

import com.careertrack.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // ✅ REMOVED passwordEncoder Bean from here
    // It already exists in AppConfig.java
    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Security Filter Chain
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        // Public URLs
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/static/**",
                                "/webjars/**",
                                "/error"
                        ).permitAll()
                        // Admin-only URLs
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // All others need auth
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "remember-me")
                        .clearAuthentication(true)
                        .permitAll()
                )
                .rememberMe(remember -> remember
                        .userDetailsService(userDetailsService)
                        .key("careertrack-secret-key")
                        .tokenValiditySeconds(7 * 24 * 60 * 60)
                        .rememberMeParameter("remember-me")
                );

        return http.build();
    }

    // ✅ Authentication Manager
    @Bean
    public AuthenticationManager authManager(
            HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder builder =
                http.getSharedObject(
                        AuthenticationManagerBuilder.class);

        builder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        return builder.build();
    }
}