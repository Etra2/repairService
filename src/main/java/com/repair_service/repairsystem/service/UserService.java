package com.repair_service.repairsystem.service;

import com.repair_service.repairsystem.entity.User;
import java.util.Optional;

public interface UserService {
    //zapisywanie uzytkownika - rejestracja
    User saveUser(User user);

    //wyszukiwanie po email
    Optional<User> findByEmail(String email);

    //wyszukiwanie po id
    Optional<User> findById(Long id);
}
