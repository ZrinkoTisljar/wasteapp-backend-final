package com.example.wasteappfinal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO koji predstavlja podatke za izmjenu postojeće vrste otpada.
 *
 * Koristi se u admin dijelu aplikacije, gdje ovlašteni korisnik može
 * ažurirati naziv, opis i status aktivacije vrste otpada.
 *
 * Validacijske anotacije osiguravaju:
 * - da naziv nije prazan
 * - da naziv i opis ne prelaze maksimalnu duljinu
 *
 * DTO se validira u kontroleru pomoću @Valid prije prosljeđivanja servisnom sloju.
 */
public class WasteTypeUpdateRequest {

    /** Novi naziv vrste otpada. Obvezno polje. */
    @NotBlank(message = "Naziv je obvezan.")
    @Size(max = 255)
    private String name;

    /** Novi opis vrste otpada (opcionalno). Maksimalno 500 znakova. */
    @Size(max = 500)
    private String description;

    /** Status aktivacije vrste otpada (true = aktivno, false = neaktivno). */
    private boolean active;

    // --- GETTERI I SETTERI ---

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
