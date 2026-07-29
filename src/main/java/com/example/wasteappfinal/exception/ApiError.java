package com.example.wasteappfinal.exception;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO (record) koji predstavlja jedinstveni i dosljedni oblik JSON odgovora
 * kada nastane pogreška u REST API-ju.
 *
 * Ovaj model se koristi u globalnom exception handleru kako bi se sve greške
 * prikazivale u ujednačenom formatu, neovisno o tome gdje su nastale.
 *
 * Polja:
 * - timestamp: trenutak kada je pogreška nastala
 * - status: HTTP statusni kod (npr. 400, 401, 404, 500)
 * - error: kratki opis pogreške (npr. "Bad Request", "Unauthorized")
 * - message: detaljna poruka za korisnika ili administratora
 * - path: URL ruta na kojoj je pogreška nastala
 * - validationErrors: mapa polja i poruka validacijskih grešaka (ako postoje)
 *
 * Record se koristi jer je immutable, jednostavan i idealan za read-only JSON odgovore.
 */
public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> validationErrors
) { }
