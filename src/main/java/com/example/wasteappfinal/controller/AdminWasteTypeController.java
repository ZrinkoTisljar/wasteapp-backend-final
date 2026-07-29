package com.example.wasteappfinal.controller;

import com.example.wasteappfinal.dto.*;
import com.example.wasteappfinal.service.WasteTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Admin REST kontroler za upravljanje vrstama otpada.
 *
 * Ovaj kontroler omogućuje CRUD operacije nad šifrarnikom vrsta otpada.
 * Koristi se u administracijskom dijelu aplikacije gdje ovlašteni korisnici
 * mogu dodavati nove vrste otpada, uređivati postojeće ili ih deaktivirati.
 *
 * Controller ne sadrži poslovnu logiku; samo delegira pozive servisnom sloju.
 */
@RestController
@RequestMapping("/api/admin/waste-types")
public class AdminWasteTypeController {

    private final WasteTypeService service;

    /**
     * Konstruktor injektira servisni sloj.
     * Preporučeni pristup jer olakšava testiranje i čini klasu immutable.
     */
    public AdminWasteTypeController(WasteTypeService service) {
        this.service = service;
    }

    /**
     * GET /api/admin/waste-types
     *
     * Vraća sve vrste otpada (aktivne i neaktivne), sortirane po nazivu.
     * Koristi se u admin panelu za pregled i upravljanje šifrarnikom.
     */
    @GetMapping
    public List<WasteTypeResponse> listAll() {
        return service.listAll();
    }

    /**
     * POST /api/admin/waste-types
     *
     * Kreira novu vrstu otpada.
     * Validacija se provodi nad WasteTypeCreateRequest DTO objektom.
     *
     * @return ResponseEntity s HTTP statusom 201 CREATED i kreiranim objektom.
     */
    @PostMapping
    public ResponseEntity<WasteTypeResponse> create(
            @Valid @RequestBody WasteTypeCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    /**
     * PUT /api/admin/waste-types/{id}
     *
     * Ažurira postojeću vrstu otpada.
     * Validacija se provodi nad WasteTypeUpdateRequest DTO objektom.
     *
     * @param id ID vrste otpada koja se ažurira
     * @return ažurirani WasteTypeResponse DTO
     */
    @PutMapping("/{id}")
    public WasteTypeResponse update(
            @PathVariable Long id,
            @Valid @RequestBody WasteTypeUpdateRequest request
    ) {
        return service.update(id, request);
    }

    /**
     * DELETE /api/admin/waste-types/{id}
     *
     * Deaktivira vrstu otpada (soft delete).
     * Ne briše zapis iz baze, već postavlja active=false.
     *
     * @param id ID vrste otpada
     * @return jednostavna poruka o uspješnoj deaktivaciji
     */
    @DeleteMapping("/{id}")
    public MessageResponse deactivate(@PathVariable Long id) {
        return service.deactivate(id);
    }
}
