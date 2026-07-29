package com.example.wasteappfinal.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT autentifikacijski filter koji se izvršava jednom po HTTP zahtjevu.
 *
 * Filter čita Authorization header, provjerava Bearer token, validira ga
 * i ako je token ispravan postavlja autentificiranog korisnika u
 * SecurityContext. Time se omogućuje da ostatak aplikacije prepozna
 * korisnika kao prijavljenog.
 *
 * Ako token ne postoji ili je neispravan, zahtjev se prosljeđuje dalje
 * bez autentifikacije.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final DatabaseUserDetailsService userDetailsService;

    /**
     * Konstruktor s dependency injectionom JWT servisa i UserDetails servisa.
     */
    public JwtAuthFilter(JwtService jwtService, DatabaseUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Glavna logika filtera:
     * - čitanje Authorization headera
     * - provjera Bearer tokena
     * - validacija tokena
     * - učitavanje korisnika
     * - postavljanje autentifikacije u SecurityContext
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Čitanje Authorization headera
        String header = request.getHeader("Authorization");

        // Ako header ne postoji ili nije Bearer token → nastavi bez autentifikacije
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Izdvajanje tokena iz headera
        String token = header.substring(7);

        // Ako token nije valjan → nastavi bez autentifikacije
        if (!jwtService.isValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Ako korisnik još nije autentificiran u ovom zahtjevu
        if (SecurityContextHolder.getContext().getAuthentication() == null) {

            // Dohvati email iz tokena
            String email = jwtService.extractEmail(token);

            // Učitaj korisnika iz baze
            var userDetails = userDetailsService.loadUserByUsername(email);

            // Kreiraj autentifikacijski objekt
            var authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            // Dodaj detalje o zahtjevu (IP, session itd.)
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Postavi autentifikaciju u SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Nastavi kroz ostatak filter lanca
        filterChain.doFilter(request, response);
    }
}
