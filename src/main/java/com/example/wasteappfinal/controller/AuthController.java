package com.example.wasteappfinal.controller;

import com.example.wasteappfinal.dto.*;
import com.example.wasteappfinal.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST kontroler koji sadrži javne endpoint-e za registraciju i prijavu korisnika.
 *
 * /api/auth/register – kreira novi korisnički račun
 * /api/auth/login – vraća JWT token nakon uspješne prijave
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    /**
     * Konstruktor – Spring automatski ubacuje AuthService (dependency injection).
     */
    public AuthController(AuthService service) {
        this.service = service;
    }

    /**
     * Endpoint za registraciju korisnika.
     *
     * @param request DTO s podacima za registraciju
     * @return MessageResponse s porukom o uspješnoj registraciji
     */
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(request));
    }

    /**
     * Endpoint za prijavu korisnika.
     *
     * @param request DTO s emailom i lozinkom
     * @return AuthResponse koji sadrži JWT token i osnovne podatke o korisniku
     */
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return service.login(request);
    }
}
