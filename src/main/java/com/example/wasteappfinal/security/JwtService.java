package com.example.wasteappfinal.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * Servis koji generira i validira JWT tokene.
 *
 * Token sadrži:
 * - subject (email korisnika)
 * - uid (ID korisnika)
 * - role (uloga korisnika)
 * - vrijeme izdavanja
 * - vrijeme isteka
 *
 * Token je potpisan HMAC-SHA ključem koji se učitava iz konfiguracije.
 */
@Service
public class JwtService {

    private final Key key;
    private final long expirationMinutes;

    /**
     * Inicijalizira JWT servis.
     *
     * @param secret tajni ključ za potpisivanje tokena (mora imati barem 32 znaka)
     * @param expirationMinutes trajanje tokena u minutama (default 120)
     */
    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-minutes:120}") long expirationMinutes
    ) {
        if (secret.length() < 32) {
            throw new IllegalArgumentException("APP_JWT_SECRET mora imati najmanje 32 znaka.");
        }

        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;
    }

    /**
     * Generira JWT token s korisničkim podacima.
     *
     * @param userId ID korisnika
     * @param email email korisnika
     * @param role uloga korisnika
     * @return potpisani JWT token
     */
    public String generateToken(Long userId, String email, String role) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject(email)
                .claim("uid", userId)
                .claim("role", role)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationMinutes * 60_000))
                .signWith(key)
                .compact();
    }

    /**
     * Izvlači email (subject) iz tokena.
     */
    public String extractEmail(String token) {
        return parse(token).getBody().getSubject();
    }

    /**
     * Provjerava je li token valjan.
     * Ako je token istekao, oštećen ili neispravno potpisan → vraća false.
     */
    public boolean isValid(String token) {
        try {
            parse(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Parsira token i vraća njegove claimove.
     * Ako token nije valjan, baca JwtException.
     */
    private Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}
