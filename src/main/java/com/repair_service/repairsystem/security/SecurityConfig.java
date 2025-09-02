package com.repair_service.repairsystem.security;

import com.repair_service.repairsystem.security.jwt.JwtAuthenticationFilter;
import com.repair_service.repairsystem.security.jwt.JwtUtils;
import com.repair_service.repairsystem.security.services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService, JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    // Kodowanie haseł
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Manager do uwierzytelniania
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Konfiguracja zabezpieczeń
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils, userDetailsService);

        http
                .csrf(csrf -> csrf.disable()) // Wyłącz CSRF dla REST API
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Publiczne strony i pliki
                        .requestMatchers(
                                "/", "/index", "/dashboard", "/repair-form", "/repair-status",
                                "/style.css", "/script.js",
                                "/css/**", "/js/**",
                                "/uploads/**"
                        ).permitAll()

                        // Publiczne endpointy autoryzacji
                        .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/register").permitAll()

                        // Endpointy klienta
                        .requestMatchers("/api/repairs/**").hasAuthority("ROLE_CLIENT")

                        // Endpointy technika
                        .requestMatchers("/api/technician/**").hasAuthority("ROLE_TECHNICIAN")

                        // Wszystko inne wymaga autoryzacji
                        .anyRequest().authenticated()
                )
                // Dodajemy filtr JWT przed UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
