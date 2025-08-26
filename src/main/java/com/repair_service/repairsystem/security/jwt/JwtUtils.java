package com.repair_service.repairsystem.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Klasa pomocnicza do obsługi JWT.
 * - generowanie tokenów
 * - walidacja tokenów
 * - wyciąganie danych z tokenów
 */
@Component
public class JwtUtils {

    // Sekret używany do podpisywania JWT (musi być długi – min. 32 znaki dla HS256!)
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    // Czas ważności tokenu w milisekundach (np. 86400000 = 24h)
    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    /**
     * Pobranie klucza HMAC SHA256 na podstawie sekretu.
     * Robimy to w jednej metodzie, aby nie powtarzać kodu.
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Generuje nowy token JWT dla podanego użytkownika.
     *
     * @param username nazwa użytkownika (email)
     * @return token JWT jako String
     */
    public String generateJwtToken(String username) {
        return Jwts.builder()
                .setSubject(username) // ustawia "subject" = email/login
                .setIssuedAt(new Date()) // czas wygenerowania
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // data wygaśnięcia
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // podpisanie kluczem
                .compact();
    }

    /**
     * Wyciąga email (subject) z tokenu JWT.
     *
     * @param token token JWT
     * @return email użytkownika
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // ustawiamy klucz weryfikacyjny
                .build()
                .parseClaimsJws(token) // sprawdzamy podpis i datę ważności
                .getBody()
                .getSubject(); // zwracamy email (subject)
    }

    /**
     * Waliduje poprawność tokenu:
     * - poprawny podpis
     * - nie wygasł
     * - nieuszkodzony
     *
     * @param authToken token JWT
     * @return true jeśli token poprawny, false w przeciwnym wypadku
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey()) // weryfikacja podpisu
                    .build()
                    .parseClaimsJws(authToken); // sprawdzanie poprawności
            return true; // Token poprawny
        } catch (MalformedJwtException e) {
            System.err.println(" Błędny JWT: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println(" JWT wygasł: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println(" JWT ma nieobsługiwany format: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println(" Pusty JWT: " + e.getMessage());
        }
        return false; // Token niepoprawny
    }
}
