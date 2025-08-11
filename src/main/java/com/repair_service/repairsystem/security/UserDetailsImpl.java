package com.repair_service.repairsystem.security;

import com.repair_service.repairsystem.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// Adapter, który pozwala Spring Security traktować encję User
// jako obiekt UserDetails (wymagany przez mechanizmy autoryzacji)

public class UserDetailsImpl implements UserDetails {

    private final Long id; // identyfikator użytkownika
    private final String email; // login użytkownika (email)
    private final String password; // hasło użytkownika
    private final Collection<? extends GrantedAuthority> authorities; // role użytkownika

    public UserDetailsImpl(Long id, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    // Tworzy obiekt UserDetailsImpl z encji User
    public static UserDetailsImpl build(User user) {
        // Tworzy listę ról (GrantedAuthority)
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(user.getRole()) // zakładam, że getRole() zwraca np. "ROLE_CLIENT"
        );

        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    // !!! w Spring Security metoda to getUsername(), nie getEmail()
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
