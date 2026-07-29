package com.example.wasteappfinal.exception;

/**
 * Custom iznimka koja predstavlja HTTP 401 Unauthorized pogrešku.
 *
 * Koristi se kada korisnik nije autentificiran, odnosno kada nedostaje
 * valjani JWT token, token je istekao ili je neispravan. Za razliku od
 * ForbiddenException (403), koji označava da je korisnik prijavljen ali
 * nema ovlasti, UnauthorizedException označava da korisnik uopće nije
 * prijavljen ili je autentifikacija neuspješna.
 *
 * Primjeri uporabe:
 * - nedostaje Authorization header
 * - JWT token je istekao ili neispravan
 * - korisnik pokušava pristupiti zaštićenoj ruti bez prijave
 *
 * Iznimka se obrađuje u globalnom exception handleru, gdje se pretvara
 * u ApiError JSON odgovor s HTTP statusom 401.
 */
public class UnauthorizedException extends RuntimeException {

    /** Stvara novu UnauthorizedException s opisnom porukom. */
    public UnauthorizedException(String message) {
        super(message);
    }
}
