package com.example.wasteappfinal.dto;

import com.example.wasteappfinal.entity.WorkOrder;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO (record) koji predstavlja radni nalog u obliku prikladnom za prikaz na frontendu.
 *
 * Record se koristi jer predstavlja nepromjenjivi (immutable) skup podataka,
 * idealan za read-only odgovore API-ja. Ovaj DTO sadrži sve informacije koje
 * frontend treba prikazati korisniku ili administratoru:
 *
 * - identifikacijske podatke (ID naloga, ID korisnika)
 * - podatke o korisniku (ime, email)
 * - podatke o vrsti otpada (ID, kod, naziv)
 * - količinu i mjernu jedinicu
 * - status naloga (CREATED, SCHEDULED, COMPLETED...)
 * - adresu preuzimanja
 * - vremenske oznake (requestedAt, scheduledFor, completedAt)
 * - napomenu korisnika
 *
 * Metoda from(WorkOrder order) služi za jednostavno i centralizirano mapiranje
 * entiteta u DTO, čime se izbjegava vraćanje cijelog entiteta prema frontendu.
 */
public record WorkOrderResponse(
        Long id,
        Long userId,
        String userName,
        String userEmail,
        Long wasteTypeId,
        String wasteTypeCode,
        String wasteTypeName,
        BigDecimal quantity,
        String unit,
        String status,
        String pickupAddress,
        LocalDateTime requestedAt,
        LocalDateTime scheduledFor,
        LocalDateTime completedAt,
        String note
) {

    /**
     * Pretvara entitet iz baze WorkOrder u WorkOrderResponse DTO koji se vraća frontendu.
     * Koristi se u servisnom sloju i kontroleru.
     */
    public static WorkOrderResponse from(WorkOrder order) {
        return new WorkOrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getUser().getDisplayName(),
                order.getUser().getEmail(),
                order.getWasteType().getId(),
                order.getWasteType().getCode(),
                order.getWasteType().getName(),
                order.getQuantity(),
                order.getUnit().name(),
                order.getStatus().name(),
                order.getPickupAddress(),
                order.getRequestedAt(),
                order.getScheduledFor(),
                order.getCompletedAt(),
                order.getNote()
        );
    }
}
