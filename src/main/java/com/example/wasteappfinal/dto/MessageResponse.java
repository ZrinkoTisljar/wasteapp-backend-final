package com.example.wasteappfinal.dto;

/**
 * DTO (record) koji predstavlja jednostavan JSON odgovor s tekstualnom porukom.
 *
 * Koristi se u situacijama kada backend treba vratiti samo informaciju o
 * uspješnosti ili statusu neke operacije, bez dodatnih podataka.
 *
 * Primjeri uporabe:
 * - potvrda uspješnog brisanja
 * - potvrda uspješnog ažuriranja
 * - informativne poruke za korisnika ili administratora
 *
 * Record se koristi jer predstavlja nepromjenjivi (immutable) skup podataka
 * i idealan je za jednostavne, read-only odgovore API-ja.
 */
public record MessageResponse(String message) { }
