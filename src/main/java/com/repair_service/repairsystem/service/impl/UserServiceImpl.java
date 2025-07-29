package com.repair_service.repairsystem.service.impl;

import com.repair_service.repairsystem.entity.User;
import com.repair_service.repairsystem.repository.UserRepository;
import com.repair_service.repairsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    //wstrzykiwanie repo
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //rejestracja uzytkownika
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    //wyszukiwanie po email
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    //wyszukiwanie po id
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
