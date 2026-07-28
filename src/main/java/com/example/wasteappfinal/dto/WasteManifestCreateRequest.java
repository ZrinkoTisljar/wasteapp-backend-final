package com.example.wasteappfinal.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO koji predstavlja podatke potrebne za stvaranje pratećeg lista (WasteManifest).
 *
 * Prateći list se uvijek generira na temelju postojećeg radnog naloga,
 * stoga je jedini obvezan podatak ID radnog naloga.
 *
 * Napomena je opcionalna i služi za dodatne informacije koje korisnik
 * ili administrator želi dodati u dokument.
 *
 * DTO se validira u kontroleru pomoću @Valid prije prosljeđivanja
 * servisnom sloju koji generira prateći list i sprema ga u bazu.
 */
public class WasteManifestCreateRequest {

    /** ID radnog naloga iz kojeg nastaje prateći list. Obvezno polje. */
    @NotNull(message = "ID radnog naloga je obvezan.")
    private Long workOrderId;

    /** Dodatna napomena (opcionalno). Maksimalno 500 znakova. */
    @Size(max = 500)
    private String note;

    // --- GETTERI I SETTERI ---

    public Long getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(Long workOrderId) { this.workOrderId = workOrderId; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
