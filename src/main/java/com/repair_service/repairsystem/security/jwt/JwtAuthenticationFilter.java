package com.repair_service.repairsystem.security.jwt;

import com.repair_service.repairsystem.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 🔍 Filtr JWT – sprawdza w każdym żądaniu czy w nagłówku Authorization
 * znajduje się poprawny token. Jeśli tak – ustawia zalogowanego użytkownika
 * w kontekście Spring Security.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = parseJwt(request); // Pobieramy token z nagłówka

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // Wyciągamy email z tokena
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // Pobieramy dane użytkownika z bazy
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Tworzymy obiekt Authentication i zapisujemy go w SecurityContext
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            System.err.println(" Błąd uwierzytelniania JWT: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    // Wyciąganie tokenu z nagłówka Authorization: "Bearer <token>"
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
