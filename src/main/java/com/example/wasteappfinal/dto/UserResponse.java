package com.example.wasteappfinal.dto;

import com.example.wasteappfinal.entity.User;
import java.time.LocalDateTime;

/**
 * DTO (record) koji predstavlja podatke o korisniku koji se smiju vratiti frontendu.
 *
 * Record se koristi jer predstavlja nepromjenjivi (immutable) skup podataka,
 * idealan za read-only odgovore API-ja.
 *
 * Sadrži:
 * - osnovne identifikacijske podatke (id, email)
 * - ulogu i tip korisnika
 * - prikazno ime (fullName ili companyName)
 * - kontakt podatke (adresa, telefon)
 * - status odobrenosti korisnika
 * - datum kreiranja računa
 *
 * Metoda from(User user) služi za jednostavno mapiranje entiteta u DTO.
 */
public record UserResponse(
        Long id,
        String email,
        String role,
        String userType,
        String displayName,
        String address,
        String phone,
        boolean approved,
        LocalDateTime createdAt
) {

    /**
     * Pretvara entitet User u UserResponse DTO.
     * Koristi se u servisnom sloju i kontroleru kako bi se izbjeglo vraćanje
     * cijelog entiteta prema frontendu.
     */
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole().name(),
                user.getUserType().name(),
                user.getDisplayName(),
                user.getAddress(),
                user.getPhone(),
                user.isApproved(),
                user.getCreatedAt()
        );
    }
}
