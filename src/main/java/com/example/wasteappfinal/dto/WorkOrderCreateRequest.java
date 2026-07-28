package com.example.wasteappfinal.dto;

import com.example.wasteappfinal.enums.QuantityUnit;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO koji predstavlja podatke potrebne za stvaranje radnog naloga.
 *
 * Sadrži validacijske anotacije koje osiguravaju:
 * - da je odabrana vrsta otpada
 * - da je količina veća od nule i pravilno formatirana
 * - da je odabrana mjerna jedinica
 * - da je adresa preuzimanja ispravna i nije prazna
 * - da napomena (ako postoji) ne prelazi maksimalnu duljinu
 *
 * DTO se koristi u kontroleru za kreiranje radnog naloga, gdje se validira
 * pomoću @Valid prije prosljeđivanja servisnom sloju.
 */
public class WorkOrderCreateRequest {

    /** ID vrste otpada koju korisnik odabire. Obvezno polje. */
    @NotNull(message = "Vrsta otpada je obvezna.")
    private Long wasteTypeId;

    /**
     * Količina otpada.
     * Mora biti veća od 0.001 i može imati najviše tri decimale.
     */
    @NotNull(message = "Količina je obvezna.")
    @DecimalMin(value = "0.001", message = "Količina mora biti veća od nule.")
    @Digits(integer = 9, fraction = 3, message = "Količina može imati najviše tri decimale.")
    private BigDecimal quantity;

    /** Mjerna jedinica (KG, L, M3...). Obvezno polje. */
    @NotNull(message = "Mjerna jedinica je obvezna.")
    private QuantityUnit unit;

    /** Adresa preuzimanja otpada. Obvezno polje, maksimalno 255 znakova. */
    @NotBlank(message = "Adresa preuzimanja je obvezna.")
    @Size(max = 255)
    private String pickupAddress;

    /** Napomena korisnika (opcionalno). Maksimalno 500 znakova. */
    @Size(max = 500)
    private String note;

    // --- GETTERI I SETTERI ---

    public Long getWasteTypeId() { return wasteTypeId; }
    public void setWasteTypeId(Long wasteTypeId) { this.wasteTypeId = wasteTypeId; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public QuantityUnit getUnit() { return unit; }
    public void setUnit(QuantityUnit unit) { this.unit = unit; }

    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
