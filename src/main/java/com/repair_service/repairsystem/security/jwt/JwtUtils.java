package com.repair_service.repairsystem.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

// klasa narzędziowa - generowanie i weryfikacja tokenów JWT

@Component
public class JwtUtils {

    // sekret do podpisywania JWT
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    // czas ważności tokenu w milisekundach
    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    // tworzenie tokenu JWT na podstawie nazwy użytkownika
    public String generateJwtToken(String username) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.builder()
                .setSubject(username) // wlaściciel tokenu
                .setIssuedAt(new Date()) // data wygenerowania
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // ważność
                .signWith(key, SignatureAlgorithm.HS256) // podpisanie
                .compact();
    }

    // pobieranie loginu (email) z tokenu JWT
    public String getUserNameFormToken(String token) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key) // klucz weryfikacji podpisu
                .build()
                .parseClaimsJwt(token) // parsowanie tokenu
                .getBody()
                .getSubject(); // zwracanie loginu
    }

    //sprawdzanie poprawności tokeniu - podpis + data ważności
    public boolean validateJwtToken(String authToken) {
        try{
            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJwt(authToken);
            return true; // token ok
        } catch (JwtException e) {
            // jezeli token jest uskodzony, wygasł lub ma zły podpis - błąd
            return false;
        }
    }
}
