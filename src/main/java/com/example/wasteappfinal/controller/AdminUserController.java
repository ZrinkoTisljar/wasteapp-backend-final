package com.example.wasteappfinal.controller;

import com.example.wasteappfinal.dto.MessageResponse;
import com.example.wasteappfinal.dto.UserResponse;
import com.example.wasteappfinal.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST kontroler za administraciju korisnika.
 *
 * Omogućuje:
 * - dohvat svih korisnika
 * - dohvat korisnika koji čekaju odobrenje
 * - odobravanje korisničkih računa
 * - brisanje korisnika (uz validaciju u servisu)
 *
 * Sve poslovne operacije delegiraju se u UserService.
 */
@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserService service;

    // Constructor injection — preporučeni način u Springu
    public AdminUserController(UserService service) {
        this.service = service;
    }

    /**
     * Vraća sve korisnike.
     * GET /api/admin/users
     */
    @GetMapping
    public List<UserResponse> listAll() {
        return service.listAll();
    }

    /**
     * Vraća korisnike koji čekaju odobrenje.
     * GET /api/admin/users/pending
     */
    @GetMapping("/pending")
    public List<UserResponse> listPending() {
        return service.listPending();
    }

    /**
     * Odobrava korisnika.
     * PATCH /api/admin/users/{id}/approve
     */
    @PatchMapping("/{id}/approve")
    public UserResponse approve(@PathVariable Long id) {
        return service.approve(id);
    }

    /**
     * Briše korisnika.
     * DELETE /api/admin/users/{id}
     *
     * Servis provjerava:
     * - ne može se obrisati administratorski račun
     * - ne može se obrisati korisnik koji ima radne naloge
     */
    @DeleteMapping("/{id}")
    public MessageResponse delete(@PathVariable Long id) {
        return service.delete(id);
    }
}
