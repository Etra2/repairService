package com.repair_service.repairsystem.repository;

import com.repair_service.repairsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//zarządzanie dostępem do danych encji User
public interface UserRepository extends JpaRepository<User, Long>{

    // szukanie użytkownika po email, optional bo user o tym email może nie istnieć
    Optional<User> findByEmail(String email);
}
