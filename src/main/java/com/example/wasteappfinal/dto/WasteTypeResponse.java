package com.example.wasteappfinal.dto;

import com.example.wasteappfinal.entity.WasteType;

/**
 * DTO (record) koji predstavlja vrstu otpada u obliku prikladnom za prikaz na frontendu.
 *
 * Record se koristi jer predstavlja nepromjenjivi (immutable) skup podataka,
 * idealan za read-only odgovore API-ja. Ovaj DTO sadrži sve informacije koje
 * frontend treba prikazati korisniku ili administratoru:
 *
 * - identifikacijske podatke (ID, kod)
 * - naziv i opis vrste otpada
 * - status aktivacije (active)
 *
 * Metoda from(WasteType type) služi za jednostavno i centralizirano mapiranje
 * entiteta u DTO, čime se izbjegava vraćanje cijelog entiteta prema frontendu.
 */
public record WasteTypeResponse(
        Long id,
        String code,
        String name,
        String description,
        boolean active
) {

    /**
     * Pretvara entitet WasteType u WasteTypeResponse DTO.
     * Koristi se u servisnom sloju i kontroleru.
     */
    public static WasteTypeResponse from(WasteType type) {
        return new WasteTypeResponse(
                type.getId(),
                type.getCode(),
                type.getName(),
                type.getDescription(),
                type.isActive()
        );
    }
}
