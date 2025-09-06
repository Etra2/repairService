package com.repair_service.repairsystem.service.impl;

import com.repair_service.repairsystem.entity.User;
import com.repair_service.repairsystem.repository.UserRepository;
import com.repair_service.repairsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementacja serwisu użytkowników.
 * Zawiera metody do zapisu, wyszukiwania po email i po ID.
 * Serwis działa jako warstwa pośrednia między kontrolerem a repozytorium.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Konstruktor z wstrzykiwaniem repozytorium użytkowników.
     * @param userRepository repozytorium użytkowników
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Zapis nowego użytkownika lub aktualizacja istniejącego.
     * @param user obiekt User do zapisania
     * @return zapisany obiekt User
     */
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Wyszukiwanie użytkownika po adresie email.
     * @param email adres email użytkownika
     * @return Optional<User> – obiekt użytkownika jeśli istnieje
     */
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Wyszukiwanie użytkownika po ID.
     * @param id identyfikator użytkownika
     * @return Optional<User> – obiekt użytkownika jeśli istnieje
     */
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
