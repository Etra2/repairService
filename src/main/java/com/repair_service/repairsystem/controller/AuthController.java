package com.repair_service.repairsystem.controller;

import com.repair_service.repairsystem.entity.User;
import com.repair_service.repairsystem.repository.UserRepository;
import com.repair_service.repairsystem.security.UserDetailsImpl;
import com.repair_service.repairsystem.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    //Logowanie – zwraca JWT
    @PostMapping("/login")
    public String authenticateUser(@Valid @RequestBody User loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication.getName());

        return jwt;
    }

    //Rejestracja nowego użytkownika

    @PostMapping("/register")
    public String registerUser(@Valid @RequestBody User signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return "Błąd: Email jest już zajęty!";
        }

        signUpRequest.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        userRepository.save(signUpRequest);

        return "Użytkownik zarejestrowany pomyślnie!";
    }
}

