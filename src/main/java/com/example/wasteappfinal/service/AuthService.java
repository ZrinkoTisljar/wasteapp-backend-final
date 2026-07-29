package com.example.wasteappfinal.service;

import com.example.wasteappfinal.dto.*;
import com.example.wasteappfinal.entity.*;
import com.example.wasteappfinal.enums.Role;
import com.example.wasteappfinal.exception.BadRequestException;
import com.example.wasteappfinal.exception.ForbiddenException;
import com.example.wasteappfinal.exception.UnauthorizedException;
import com.example.wasteappfinal.repository.UserRepository;
import com.example.wasteappfinal.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Servisni sloj koji sadrži poslovnu logiku registracije i prijave korisnika.
 *
 * Ovdje se provodi:
 * - validacija podataka za registraciju
 * - kreiranje korisničkog računa
 * - provjera vjerodajnica prilikom prijave
 * - generiranje JWT tokena
 *
 * Servis koristi custom iznimke (BadRequestException, UnauthorizedException, ForbiddenException)
 * kako bi se osigurala dosljedna obrada pogrešaka u REST API-ju.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Konstruktor s dependency injectionom potrebnih komponenti.
     */
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Registrira novog korisnika.
     * Provodi validaciju, hashira lozinku i sprema korisnika u bazu.
     * Ako korisnik već postoji, baca BadRequestException.
     */
    @Transactional
    public MessageResponse register(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new BadRequestException("Korisnik s tom adresom e-pošte već postoji.");
        }

        validateUserTypeFields(request);

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setUserType(request.getUserType());
        user.setFullName(clean(request.getFullName()));
        user.setCompanyName(clean(request.getCompanyName()));
        user.setOib(clean(request.getOib()));
        user.setAddress(request.getAddress().trim());
        user.setPhone(clean(request.getPhone()));

        userRepository.save(user);

        return new MessageResponse("Registracija je uspješna. Račun mora odobriti administrator.");
    }

    /**
     * Prijavljuje korisnika.
     * Provjerava vjerodajnice, status odobrenosti i generira JWT token.
     * Ako su podaci neispravni, baca UnauthorizedException.
     * Ako račun nije odobren, baca ForbiddenException.
     */
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.getEmail().trim())
                .orElseThrow(() -> new UnauthorizedException("Adresa e-pošte ili lozinka nisu ispravni."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Adresa e-pošte ili lozinka nisu ispravni.");
        }

        if (!user.isApproved()) {
            throw new ForbiddenException("Račun još nije odobrio administrator.");
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );

        return new AuthResponse(token, user.getEmail(), user.getRole().name(), user.getUserType().name());
    }

    /**
     * Validira polja ovisno o tipu korisnika (građanin ili tvrtka).
     * Baca BadRequestException ako obvezna polja nedostaju.
     */
    private void validateUserTypeFields(RegisterRequest request) {
        if (request.getUserType() == UserType.CITIZEN
                && (request.getFullName() == null || request.getFullName().isBlank())) {
            throw new BadRequestException("Ime i prezime obvezni su za građanina.");
        }

        if (request.getUserType() == UserType.COMPANY) {
            if (request.getCompanyName() == null || request.getCompanyName().isBlank()) {
                throw new BadRequestException("Naziv tvrtke je obvezan.");
            }
            if (request.getOib() == null || request.getOib().isBlank()) {
                throw new BadRequestException("OIB je obvezan za tvrtku.");
            }
        }
    }

    /**
     * Pomoćna metoda koja vraća trimanu vrijednost ili null ako je prazna.
     */
    private String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
