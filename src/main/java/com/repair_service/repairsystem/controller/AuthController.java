package com.repair_service.repairsystem.controller;

import com.repair_service.repairsystem.dto.LoginRequestDto;
import com.repair_service.repairsystem.entity.User;
import com.repair_service.repairsystem.repository.UserRepository;
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

    /**
     * 🔐 Logowanie użytkownika
     * Przyjmuje LoginRequestDto (email + hasło), uwierzytelnia użytkownika
     * i zwraca token JWT.
     */
    @PostMapping("/login")
    public String authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        // Tworzymy obiekt Authentication na podstawie emaila i hasła
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // Ustawiamy użytkownika jako zalogowanego w kontekście Security
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generujemy token JWT na podstawie emaila (username)
        String jwt = jwtUtils.generateJwtToken(authentication.getName());

        return jwt; // Zwracamy token
    }

    /**
     * 📝 Rejestracja nowego użytkownika
     * Przyjmuje encję User w JSON (email, hasło, fullName, role),
     * sprawdza czy email jest wolny i zapisuje użytkownika do bazy.
     */
    @PostMapping("/register")
    public String registerUser(@Valid @RequestBody User signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return "Błąd: Email jest już zajęty!";
        }

        // Hasło musi być zakodowane przed zapisaniem w bazie
        signUpRequest.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        userRepository.save(signUpRequest);

        return "✅ Użytkownik zarejestrowany pomyślnie!";
    }
}
