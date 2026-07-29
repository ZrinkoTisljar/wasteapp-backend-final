package com.example.wasteappfinal.security;

import com.example.wasteappfinal.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementacija UserDetailsService sučelja koje Spring Security koristi
 * za učitavanje korisnika iz baze podataka prilikom autentifikacije.
 *
 * Ovdje se dohvaća korisnik prema adresi e-pošte, provjerava njegovo postojanje
 * i kreira UserDetails objekt koji Spring Security koristi za provjeru lozinke
 * i dodjelu uloga (autoriteti).
 *
 * Ako korisnik ne postoji, baca se UsernameNotFoundException — iznimka koju
 * Spring Security očekuje i automatski obrađuje.
 */
@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Konstruktor s dependency injectionom repozitorija.
     */
    public DatabaseUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Učitava korisnika iz baze prema email adresi.
     * Ako korisnik ne postoji, baca UsernameNotFoundException.
     *
     * @param email email adresa korisnika
     * @return UserDetails objekt koji Spring Security koristi za autentifikaciju
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("Korisnik nije pronađen."));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())          // korisničko ime = email
                .password(user.getPasswordHash())       // hashirana lozinka
                .roles(user.getRole().name())           // uloga (Spring dodaje ROLE_ prefiks)
                .build();
    }
}
