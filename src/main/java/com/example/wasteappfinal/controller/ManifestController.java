package com.example.wasteappfinal.controller;

import com.example.wasteappfinal.dto.WasteManifestResponse;
import com.example.wasteappfinal.service.WasteManifestService;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * USER REST kontroler za prateće listove (manifeste).
 *
 * Ovaj kontroler omogućuje prijavljenim korisnicima:
 * - pregled vlastitih pratećih listova,
 * - dohvat pojedinačnog pratećeg lista uz provjeru pristupa,
 * - preuzimanje PDF verzije pratećeg lista.
 *
 * Provjera pristupa se temelji na ulozi korisnika:
 * - ROLE_ADMIN ima pristup svim manifestima,
 * - obični korisnik ima pristup samo vlastitim manifestima.
 */
@RestController
@RequestMapping("/api/manifests")
public class ManifestController {

    private final WasteManifestService service;

    /**
     * Konstruktor injektira servisni sloj.
     * Preporučeni pristup jer olakšava testiranje i čini klasu immutable.
     */
    public ManifestController(WasteManifestService service) {
        this.service = service;
    }

    /**
     * GET /api/manifests/mine
     *
     * Vraća sve prateće listove prijavljenog korisnika.
     * Sortirano po datumu izdavanja (najnoviji prvi).
     */
    @GetMapping("/mine")
    public List<WasteManifestResponse> mine(Authentication authentication) {
        return service.listMine(authentication.getName());
    }

    /**
     * GET /api/manifests/{id}
     *
     * Dohvaća prateći list uz provjeru pristupa.
     * Admin vidi sve, korisnik samo svoje.
     *
     * @param id ID pratećeg lista
     * @param authentication Spring Security objekt s identitetom korisnika
     */
    @GetMapping("/{id}")
    public WasteManifestResponse get(@PathVariable Long id, Authentication authentication) {
        return service.getAccessible(id, authentication.getName(), isAdmin(authentication));
    }

    /**
     * GET /api/manifests/{id}/pdf
     *
     * Generira PDF dokument pratećeg lista.
     * Provodi provjeru pristupa prije generiranja.
     *
     * ResponseEntity se koristi za postavljanje Content-Type i Content-Disposition
     * zaglavlja kako bi preglednik ispravno prikazao PDF.
     */
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> pdf(@PathVariable Long id, Authentication authentication) {
        boolean admin = isAdmin(authentication);
        var pdf = service.generatePdf(id, authentication.getName(), admin);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + pdf.fileName() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf.content());
    }

    /**
     * Provjerava ima li korisnik administratorsku ulogu.
     * Koristi se za kontrolu pristupa manifestima.
     */
    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}
