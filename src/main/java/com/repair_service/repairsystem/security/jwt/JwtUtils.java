package com.repair_service.repairsystem.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

//Klasa narzędziowa - generowanie i weryfikacja tokenów JWT

@Component
public class JwtUtils {

    // Sekret do podpisywania JWT (w application.properties!)
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    // Czas ważności tokenu w milisekundach
    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    // Generowanie tokenu JWT na podstawie nazwy użytkownika
    public String generateJwtToken(String username) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.builder()
                .setSubject(username) // Właściciel tokenu
                .setIssuedAt(new Date()) // Data wygenerowania
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Ważność
                .signWith(key, SignatureAlgorithm.HS256) // Podpisanie
                .compact();
    }

    // Pobieranie loginu (email) z tokenu JWT

    public String getUserNameFromJwtToken(String token) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key) // Klucz do weryfikacji podpisu
                .build()
                .parseClaimsJws(token) // Parsowanie podpisanego tokenu
                .getBody()
                .getSubject(); // Zwracanie loginu
    }

    //Sprawdzanie poprawności tokenu - podpis + data ważności

    public boolean validateJwtToken(String authToken) {
        try {
            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(authToken); // Sprawdza podpis i datę ważności
            return true; // Token OK
        } catch (JwtException e) {
            // Token uszkodzony, wygasł lub ma zły podpis
            return false;
        }
    }
}
