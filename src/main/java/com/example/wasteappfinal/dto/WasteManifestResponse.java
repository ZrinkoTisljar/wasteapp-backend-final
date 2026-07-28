package com.example.wasteappfinal.dto;

import com.example.wasteappfinal.entity.WasteManifest;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO (record) koji predstavlja prateći list (WasteManifest) u obliku
 * prikladnom za prikaz na frontendu.
 *
 * Record se koristi jer predstavlja nepromjenjivi (immutable) skup podataka,
 * idealan za read-only odgovore API-ja. Ovaj DTO sadrži sve informacije koje
 * frontend treba prikazati korisniku ili administratoru:
 *
 * - osnovne identifikacijske podatke (ID, manifestNumber)
 * - podatke o radnom nalogu iz kojeg je nastao prateći list
 * - podatke o korisniku (ime, email)
 * - podatke o vrsti otpada (naziv, količina, jedinica)
 * - adresu preuzimanja
 * - status radnog naloga
 * - napomenu i datum izdavanja pratećeg lista
 *
 * Metoda from(WasteManifest manifest) služi za jednostavno i centralizirano
 * mapiranje entiteta u DTO, čime se izbjegava vraćanje cijelog entiteta
 * prema frontendu.
 */
public record WasteManifestResponse(
        Long id,
        String manifestNumber,
        Long workOrderId,
        LocalDateTime issuedAt,
        String note,
        String userName,
        String userEmail,
        String wasteTypeName,
        BigDecimal quantity,
        String unit,
        String pickupAddress,
        String workOrderStatus
) {

    /**
     * Pretvara entitet WasteManifest u WasteManifestResponse DTO.
     * Koristi se u servisnom sloju i kontroleru.
     */
    public static WasteManifestResponse from(WasteManifest manifest) {
        var order = manifest.getWorkOrder();
        return new WasteManifestResponse(
                manifest.getId(),
                manifest.getManifestNumber(),
                order.getId(),
                manifest.getIssuedAt(),
                manifest.getNote(),
                order.getUser().getDisplayName(),
                order.getUser().getEmail(),
                order.getWasteType().getName(),
                order.getQuantity(),
                order.getUnit().name(),
                order.getPickupAddress(),
                order.getStatus().name()
        );
    }
}
