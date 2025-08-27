package com.repair_service.repairsystem.controller;

import com.repair_service.repairsystem.dto.auth.AuthRequestDto;
import com.repair_service.repairsystem.dto.LoginRequestDto;
import com.repair_service.repairsystem.entity.User;
import com.repair_service.repairsystem.repository.UserRepository;
import com.repair_service.repairsystem.security.UserDetailsImpl;
import com.repair_service.repairsystem.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    // ==================== LOGIN ====================
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {

        // Uwierzytelnienie użytkownika w Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // Zapisanie kontekstu bezpieczeństwa
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Wygenerowanie tokena JWT
        String jwt = jwtUtils.generateJwtToken(loginRequest.getEmail());

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Tworzymy mapę z tokenem i danymi użytkownika
        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);

        Map<String, Object> userData = new HashMap<>();
        userData.put("id", userDetails.getId());
        userData.put("email", userDetails.getEmail());
        userData.put("role", userDetails.getRole());

        response.put("user", userData);

        return ResponseEntity.ok(response);
    }

    // ==================== REGISTER ====================
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody AuthRequestDto signUpRequest) {
        // Sprawdzenie, czy email już istnieje w bazie
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Błąd: Email jest już zajęty!");
            return ResponseEntity.badRequest().body(error);
        }

        // Tworzymy nowego użytkownika z danych z DTO
        User user = new User();
        user.setFullName(signUpRequest.getFullName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword())); // hashujemy hasło
        user.setRole("ROLE_CLIENT"); // nadajemy rolę klienta domyślnie

        // Zapis do bazy
        userRepository.save(user);

        Map<String, String> success = new HashMap<>();
        success.put("message", "Użytkownik zarejestrowany pomyślnie!");
        return ResponseEntity.ok(success);
    }
}
