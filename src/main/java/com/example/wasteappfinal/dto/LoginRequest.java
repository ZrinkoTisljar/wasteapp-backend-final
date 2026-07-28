package com.example.wasteappfinal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO koji predstavlja podatke koje korisnik šalje prilikom prijave (login).
 *
 * Sadrži validacijske anotacije koje osiguravaju:
 * - da je email unesen i u ispravnom formatu
 * - da lozinka nije prazna
 *
 * DTO se koristi u autentikacijskom kontroleru, gdje se validira pomoću @Valid
 * prije prosljeđivanja servisnom sloju koji provjerava korisničke podatke
 * i generira JWT token.
 */
public class LoginRequest {

    /** Email korisnika – obvezan i mora biti u ispravnom formatu. */
    @NotBlank(message = "Adresa e-pošte je obvezna.")
    @Email(message = "Adresa e-pošte nije ispravna.")
    private String email;

    /** Lozinka – obvezna, bez dodatnih ograničenja jer se provjerava u servisu. */
    @NotBlank(message = "Lozinka je obvezna.")
    private String password;

    // --- GETTERI I SETTERI ---

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
