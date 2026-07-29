package com.example.wasteappfinal.exception;

/**
 * Custom iznimka koja predstavlja HTTP 404 Not Found pogrešku.
 *
 * Koristi se kada traženi resurs ne postoji u sustavu — primjerice,
 * kada korisnik, radni nalog, vrsta otpada ili prateći list ne mogu
 * biti pronađeni u bazi podataka.
 *
 * Ova iznimka omogućuje backendu da jasno i dosljedno komunicira
 * situacije u kojima je klijent zatražio nešto što ne postoji.
 *
 * Iznimka se obrađuje u globalnom exception handleru, gdje se pretvara
 * u ApiError JSON odgovor s HTTP statusom 404.
 */
public class NotFoundException extends RuntimeException {

    /** Stvara novu NotFoundException s opisnom porukom. */
    public NotFoundException(String message) {
        super(message);
    }
}
