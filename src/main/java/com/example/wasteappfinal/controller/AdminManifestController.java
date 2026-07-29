package com.example.wasteappfinal.controller;

import com.example.wasteappfinal.dto.WasteManifestCreateRequest;
import com.example.wasteappfinal.dto.WasteManifestResponse;
import com.example.wasteappfinal.service.WasteManifestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * ADMIN REST kontroler za upravljanje pratećim listovima (manifestima).
 *
 * Ovaj kontroler omogućuje administraciji:
 * - kreiranje novih pratećih listova,
 * - pregled svih izdanih pratećih listova.
 *
 * Controller ne sadrži poslovnu logiku; samo delegira pozive servisnom sloju.
 */
@RestController
@RequestMapping("/api/admin/manifests")
public class AdminManifestController {

    private final WasteManifestService service;

    /**
     * Konstruktor injektira servisni sloj.
     * Preporučeni pristup jer olakšava testiranje i čini klasu immutable.
     */
    public AdminManifestController(WasteManifestService service) {
        this.service = service;
    }

    /**
     * POST /api/admin/manifests
     *
     * Kreira novi prateći list za radni nalog.
     * Validacija se provodi nad WasteManifestCreateRequest DTO objektom.
     *
     * @return ResponseEntity s HTTP statusom 201 CREATED i kreiranim manifestom.
     */
    @PostMapping
    public ResponseEntity<WasteManifestResponse> create(
            @Valid @RequestBody WasteManifestCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(request));
    }

    /**
     * GET /api/admin/manifests
     *
     * Vraća sve prateće listove, sortirane po datumu izdavanja (najnoviji prvi).
     * Koristi se u admin panelu za pregled svih manifest-a.
     */
    @GetMapping
    public List<WasteManifestResponse> listAll() {
        return service.listAll();
    }
}
