package com.repair_service.repairsystem.security.services;

import com.repair_service.repairsystem.entity.User;
import com.repair_service.repairsystem.repository.UserRepository;
import com.repair_service.repairsystem.security.UserDetailsImpl; // <-- dodaj import
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*Klasa odpowiedzialna za ładowanie użytkownika z bazy po adresie e-mail.
 Spring Security wywołuje tę klasę podczas logowania.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*Wyszukuje użytkownika w bazie po e-mailu.
     Jeśli nie znajdzie — rzuca wyjątek.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Użytkownik o adresie " + email + " nie istnieje"));

        return UserDetailsImpl.build(user);
    }
}
