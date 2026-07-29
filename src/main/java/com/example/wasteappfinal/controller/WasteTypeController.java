package com.example.wasteappfinal.controller;

import com.example.wasteappfinal.dto.WasteTypeResponse;
import com.example.wasteappfinal.service.WasteTypeService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST kontroler za dohvat vrsta otpada iz referentnog šifrarnika.
 *
 * Ovaj endpoint koriste prijavljeni korisnici prilikom popunjavanja obrasca
 * za prijavu odlaganja otpada. Vraćaju se isključivo aktivne vrste otpada,
 * sortirane po nazivu, u obliku DTO objekata spremnih za prikaz na frontendu.
 *
 * Arhitektura:
 * Controller -> Service -> Repository
 *
 * Controller ne sadrži poslovnu logiku; samo delegira poziv servisnom sloju.
 */
@RestController
@RequestMapping("/api/reference/waste-types")
public class WasteTypeController {

    private final WasteTypeService service;

    /**
     * Konstruktor injektira servisni sloj.
     * Preporučeni pristup jer omogućuje lakše testiranje i čini klasu immutable.
     */
    public WasteTypeController(WasteTypeService service) {
        this.service = service;
    }

    /**
     * GET /api/reference/waste-types
     *
     * Vraća listu aktivnih vrsta otpada.
     * Koristi se u korisničkom obrascu za odabir vrste otpada.
     *
     * @return lista WasteTypeResponse DTO objekata
     */
    @GetMapping
    public List<WasteTypeResponse> listActive() {
        return service.listActive();
    }
}
