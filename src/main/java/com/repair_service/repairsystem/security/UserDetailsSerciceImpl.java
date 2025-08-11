package com.repair_service.repairsystem.security;

import com.repair_service.repairsystem.entity.User;
import com.repair_service.repairsystem.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//implementaja interfejsu UserDetailsService - mówi Spring Security,
// jak znaleźć użtkownika w bazie danych po loginie - email

@Service
public class UserDetailsSerciceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsSerciceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //szukanie użytkownika po email, jeśli brak to wyjątek
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika: " + email));

        // zwracanie obiektu w formacie akceptowanym przez Spring Security
        return UserDetailsImpl.build(user);
    }
}
