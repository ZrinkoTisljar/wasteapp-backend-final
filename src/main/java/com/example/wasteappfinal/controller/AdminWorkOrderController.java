package com.example.wasteappfinal.controller;

import com.example.wasteappfinal.dto.ScheduleWorkOrderRequest;
import com.example.wasteappfinal.dto.WorkOrderResponse;
import com.example.wasteappfinal.enums.WorkOrderStatus;
import com.example.wasteappfinal.service.WorkOrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * ADMIN REST kontroler za upravljanje radnim nalozima.
 *
 * Ovaj kontroler omogućuje administraciji:
 * - pregled svih radnih naloga,
 * - filtriranje naloga po statusu, vrsti otpada, korisniku i adresi,
 * - zakazivanje termina odvoza,
 * - označavanje naloga kao završenog.
 *
 * Controller ne sadrži poslovnu logiku; samo delegira pozive servisnom sloju.
 */
@RestController
@RequestMapping("/api/admin/work-orders")
public class AdminWorkOrderController {

    private final WorkOrderService service;

    /**
     * Konstruktor injektira servisni sloj.
     * Preporučeni pristup jer olakšava testiranje i čini klasu immutable.
     */
    public AdminWorkOrderController(WorkOrderService service) {
        this.service = service;
    }

    /**
     * GET /api/admin/work-orders
     *
     * Vraća sve radne naloge, sortirane po datumu zahtjeva (najnoviji prvi).
     * Koristi se u admin panelu za pregled svih naloga.
     */
    @GetMapping
    public List<WorkOrderResponse> listAll() {
        return service.listAll();
    }

    /**
     * GET /api/admin/work-orders/filter
     *
     * Dinamičko filtriranje radnih naloga.
     * Svi parametri su opcionalni, što omogućuje fleksibilno pretraživanje.
     *
     * @param status status naloga (opcionalno)
     * @param wasteTypeCode šifra vrste otpada (opcionalno)
     * @param userEmail email korisnika (opcionalno)
     * @param pickupAddress adresa preuzimanja (opcionalno)
     * @return filtrirana lista naloga
     */
    @GetMapping("/filter")
    public List<WorkOrderResponse> filter(
            @RequestParam(required = false) WorkOrderStatus status,
            @RequestParam(required = false) String wasteTypeCode,
            @RequestParam(required = false) String userEmail,
            @RequestParam(required = false) String pickupAddress
    ) {
        return service.filter(status, wasteTypeCode, userEmail, pickupAddress);
    }

    /**
     * PATCH /api/admin/work-orders/{id}/schedule
     *
     * Zakazuje termin odvoza radnog naloga.
     * Validacija se provodi nad ScheduleWorkOrderRequest DTO objektom.
     *
     * @param id ID naloga
     * @param request DTO s datumom i vremenom zakazivanja
     * @return ažurirani radni nalog
     */
    @PatchMapping("/{id}/schedule")
    public WorkOrderResponse schedule(
            @PathVariable Long id,
            @Valid @RequestBody ScheduleWorkOrderRequest request
    ) {
        return service.schedule(id, request);
    }

    /**
     * PATCH /api/admin/work-orders/{id}/complete
     *
     * Označava radni nalog kao završen.
     * Dozvoljeno samo ako je nalog prethodno zakazan.
     *
     * @param id ID naloga
     * @return ažurirani radni nalog
     */
    @PatchMapping("/{id}/complete")
    public WorkOrderResponse complete(@PathVariable Long id) {
        return service.complete(id);
    }
}
