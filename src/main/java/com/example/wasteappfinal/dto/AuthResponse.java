package com.example.wasteappfinal.dto;

/**
 * DTO odgovor nakon uspješne autentikacije.
 *
 * Record predstavlja nepromjenjivi (immutable) skup podataka
 * koji se vraća klijentu nakon prijave:
 * - JWT token za daljnje zahtjeve
 * - email prijavljenog korisnika
 * - uloga korisnika (ADMIN / USER)
 * - tip korisnika (CITIZEN / COMPANY)
 *
 * Java record automatski generira:
 * - final polja
 * - konstruktor
 * - metode za dohvat vrijednosti (token(), email(), role(), userType())
 * - equals(), hashCode(), toString()
 *
  */
public record AuthResponse(
        String token,
        String email,
        String role,
        String userType
) { }