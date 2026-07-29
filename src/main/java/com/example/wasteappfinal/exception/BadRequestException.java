package com.example.wasteappfinal.exception;

/**
 * Custom iznimka koja predstavlja HTTP 400 Bad Request pogrešku.
 *
 * Koristi se kada klijent pošalje neispravne ili nedovoljne podatke,
 * odnosno kada zahtjev ne može biti ispravno obrađen zbog greške
 * uzrokovane korisničkim unosom.
 *
 * Primjeri uporabe:
 * - neispravni parametri u requestu
 * - nedostajući obvezni podaci
 * - logičke greške (npr. pokušaj zakazivanja odvoza u prošlosti)
 *
 * Iznimka se obrađuje u globalnom exception handleru, gdje se pretvara
 * u ApiError JSON odgovor s HTTP statusom 400.
 */
public class BadRequestException extends RuntimeException {

    /** Stvara novu BadRequestException s opisnom porukom. */
    public BadRequestException(String message) {
        super(message);
    }
}
