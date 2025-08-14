package com.repair_service.repairsystem.security;

import com.repair_service.repairsystem.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/*Implementacja UserDetails – Spring Security używa tej klasy
 do przechowywania danych o zalogowanym użytkowniku.
 */
public class UserDetailsImpl implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    // Metoda tworzy obiekt UserDetailsImpl na podstawie encji User z bazy.

    public static UserDetailsImpl build(User user) {
        // Tworzymy listę ról na podstawie pola "role" (np. "ROLE_CLIENT")
        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(user.getRole()));

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

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Konto zawsze aktywne
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Konto nigdy nie jest zablokowane
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Hasło zawsze ważne
    }

    @Override
    public boolean isEnabled() {
        return true; // Konto zawsze aktywne
    }
}
