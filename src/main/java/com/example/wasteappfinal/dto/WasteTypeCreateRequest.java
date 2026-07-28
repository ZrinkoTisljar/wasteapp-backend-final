package com.example.wasteappfinal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO koji predstavlja podatke potrebne za dodavanje nove vrste otpada.
 *
 * Koristi se u admin dijelu aplikacije, gdje ovlašteni korisnik može
 * dodati novu vrstu otpada u šifrarnik. Validacijske anotacije osiguravaju:
 * - da kod i naziv nisu prazni
 * - da polja ne prelaze maksimalnu dopuštenu duljinu
 * - da opis (ako postoji) ne prelazi 500 znakova
 *
 * DTO se validira u kontroleru pomoću @Valid prije prosljeđivanja servisnom sloju.
 */
public class WasteTypeCreateRequest {

    /** Jedinstveni kod vrste otpada (npr. PLASTIC, PAPER). Obvezno polje. */
    @NotBlank(message = "Kod je obvezan.")
    @Size(max = 50)
    private String code;

    /** Naziv vrste otpada koji se prikazuje korisnicima. Obvezno polje. */
    @NotBlank(message = "Naziv je obvezan.")
    @Size(max = 255)
    private String name;

    /** Opis vrste otpada (opcionalno). Maksimalno 500 znakova. */
    @Size(max = 500)
    private String description;

    // --- GETTERI I SETTERI ---

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
