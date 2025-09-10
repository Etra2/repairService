package com.repair_service.repairsystem.repository;

import com.repair_service.repairsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Zarządzanie dostępem do danych encji User

public interface UserRepository extends JpaRepository<User, Long> {

    // Szukanie użytkownika po emailu - Zwraca Optional<User>, ponieważ użytkownik o tym emailu może nie istnieć
    Optional<User> findByEmail(String email);

    // Sprawdzenie, czy istnieje użytkownik o podanym emailu
    boolean existsByEmail(String email);
}
