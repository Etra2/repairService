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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils, userDetailsService);
        JwtDebugFilter jwtDebugFilter = new JwtDebugFilter(); // filtr debugowy

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // public
                        .requestMatchers("/", "/index", "/style.css", "/script.js", "/css/**", "/js/**", "/uploads/**").permitAll()
                        .requestMatchers("/dashboard").permitAll() // HTML dashboard dostępny, ale fetch wymaga tokena
                        .requestMatchers("/repair-form", "/repair-status").permitAll() // formularz i status publiczne


                        // technik – widok i API
                        .requestMatchers("/technician/repairs").hasAuthority("ROLE_TECHNICIAN")
                        .requestMatchers("/api/technician/**").hasAuthority("ROLE_TECHNICIAN")

                        // klient – API
                        .requestMatchers("/api/client/**").hasAuthority("ROLE_CLIENT")


                        // logowanie/rejestracja
                        .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/register").permitAll()

                        // wszystko inne autoryzacja
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtDebugFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}


