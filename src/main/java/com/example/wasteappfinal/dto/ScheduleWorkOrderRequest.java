package com.example.wasteappfinal.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * DTO koji predstavlja datum i vrijeme koje administrator odabire
 * za planirani odvoz otpada (zakazivanje radnog naloga).
 *
 * Validacijske anotacije osiguravaju:
 * - da je datum obvezan
 * - da je odabrani datum u budućnosti (nije dopušteno zakazati odvoz unatrag)
 *
 * DTO se koristi u admin dijelu aplikacije, gdje ovlašteni korisnik
 * može zakazati termin odvoza za postojeći radni nalog.
 */
public class ScheduleWorkOrderRequest {

    /** Datum i vrijeme planiranog odvoza. Mora biti u budućnosti. */
    @NotNull(message = "Datum odvoza je obvezan.")
    @Future(message = "Datum odvoza mora biti u budućnosti.")
    private LocalDateTime scheduledFor;

    // --- GETTERI I SETTERI ---

    public LocalDateTime getScheduledFor() { return scheduledFor; }
    public void setScheduledFor(LocalDateTime scheduledFor) { this.scheduledFor = scheduledFor; }
}
