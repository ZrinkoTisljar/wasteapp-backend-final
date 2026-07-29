package com.example.wasteappfinal.exception;

/**
 * Custom iznimka koja predstavlja HTTP 403 Forbidden pogrešku.
 *
 * Koristi se kada autentificirani korisnik nema potrebne ovlasti
 * za izvođenje određene radnje. Za razliku od UnauthorizedException (401),
 * koji označava da korisnik nije prijavljen, ForbiddenException označava
 * da je korisnik prijavljen, ali mu pristup nije dopušten.
 *
 * Primjeri uporabe:
 * - korisnik pokušava pristupiti tuđem radnom nalogu
 * - korisnik bez administratorske uloge pokušava obaviti admin radnju
 * - pokušaj izmjene resursa koji ne pripada korisniku
 *
 * Iznimka se obrađuje u globalnom exception handleru, gdje se pretvara
 * u ApiError JSON odgovor s HTTP statusom 403.
 */
public class ForbiddenException extends RuntimeException {

    /** Stvara novu ForbiddenException s opisnom porukom. */
    public ForbiddenException(String message) {
        super(message);
    }
}
