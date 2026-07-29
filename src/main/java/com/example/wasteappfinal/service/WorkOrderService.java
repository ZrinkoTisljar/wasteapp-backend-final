package com.example.wasteappfinal.service;

import com.example.wasteappfinal.dto.*;
import com.example.wasteappfinal.entity.*;
import com.example.wasteappfinal.enums.WorkOrderStatus;
import com.example.wasteappfinal.exception.BadRequestException;
import com.example.wasteappfinal.exception.NotFoundException;
import com.example.wasteappfinal.repository.UserRepository;
import com.example.wasteappfinal.repository.WasteTypeRepository;
import com.example.wasteappfinal.repository.WorkOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servisni sloj za upravljanje radnim nalozima.
 *
 * Ovdje se nalazi poslovna logika za:
 * - kreiranje naloga,
 * - dohvat korisničkih i administracijskih naloga,
 * - filtriranje,
 * - zakazivanje termina odvoza,
 * - označavanje naloga kao završenog.
 *
 * Controller sloj delegira sve operacije ovom servisu.
 */
@Service
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final UserRepository userRepository;
    private final WasteTypeRepository wasteTypeRepository;

    /**
     * Konstruktor injektira potrebne repozitorije.
     * Preporučeni pristup jer olakšava testiranje i čini klasu immutable.
     */
    public WorkOrderService(
            WorkOrderRepository workOrderRepository,
            UserRepository userRepository,
            WasteTypeRepository wasteTypeRepository
    ) {
        this.workOrderRepository = workOrderRepository;
        this.userRepository = userRepository;
        this.wasteTypeRepository = wasteTypeRepository;
    }

    /**
     * Kreira radni nalog za prijavljenog korisnika.
     *
     * Validira:
     * - korisnika,
     * - vrstu otpada,
     * - aktivnost vrste otpada.
     *
     * @param email email korisnika (iz JWT tokena)
     * @param request DTO s podacima za kreiranje naloga
     * @return kreirani radni nalog kao DTO
     */
    @Transactional
    public WorkOrderResponse createForUser(String email, WorkOrderCreateRequest request) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new NotFoundException("Korisnik nije pronađen."));

        WasteType wasteType = wasteTypeRepository.findById(request.getWasteTypeId())
                .orElseThrow(() -> new NotFoundException("Vrsta otpada nije pronađena."));

        if (!wasteType.isActive()) {
            throw new BadRequestException("Odabrana vrsta otpada nije aktivna.");
        }

        WorkOrder order = new WorkOrder();
        order.setUser(user);
        order.setWasteType(wasteType);
        order.setQuantity(request.getQuantity());
        order.setUnit(request.getUnit());
        order.setPickupAddress(request.getPickupAddress().trim());
        order.setNote(clean(request.getNote()));

        return WorkOrderResponse.from(workOrderRepository.save(order));
    }

    /**
     * Vraća sve radne naloge prijavljenog korisnika.
     * Sortirano po datumu zahtjeva (najnoviji prvi).
     */
    @Transactional(readOnly = true)
    public List<WorkOrderResponse> listMine(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new NotFoundException("Korisnik nije pronađen."));

        return workOrderRepository.findAllByUserIdOrderByRequestedAtDesc(user.getId())
                .stream().map(WorkOrderResponse::from).toList();
    }

    /**
     * Admin: vraća sve radne naloge.
     */
    @Transactional(readOnly = true)
    public List<WorkOrderResponse> listAll() {
        return workOrderRepository.findAllByOrderByRequestedAtDesc()
                .stream().map(WorkOrderResponse::from).toList();
    }

    /**
     * Admin: dinamičko filtriranje radnih naloga.
     * Svi parametri su opcionalni.
     */
    @Transactional(readOnly = true)
    public List<WorkOrderResponse> filter(
            WorkOrderStatus status,
            String wasteTypeCode,
            String userEmail,
            String pickupAddress
    ) {
        return workOrderRepository.filterAdmin(status, wasteTypeCode, userEmail, pickupAddress)
                .stream().map(WorkOrderResponse::from).toList();
    }

    /**
     * Zakazuje termin odvoza radnog naloga.
     *
     * Validira:
     * - termin mora biti u budućnosti,
     * - nalog ne smije biti završen ili otkazan.
     */
    @Transactional
    public WorkOrderResponse schedule(Long id, ScheduleWorkOrderRequest request) {
        WorkOrder order = findOrder(id);

        if (request.getScheduledFor().isBefore(LocalDateTime.now())
                || request.getScheduledFor().isEqual(LocalDateTime.now())) {
            throw new BadRequestException("Termin odvoza mora biti u budućnosti.");
        }

        if (order.getStatus() == WorkOrderStatus.COMPLETED ||
                order.getStatus() == WorkOrderStatus.CANCELLED) {
            throw new BadRequestException("Završen ili otkazan nalog nije moguće zakazati.");
        }

        order.setScheduledFor(request.getScheduledFor());
        order.setStatus(WorkOrderStatus.SCHEDULED);

        return WorkOrderResponse.from(workOrderRepository.save(order));
    }

    /**
     * Označava radni nalog kao završen.
     * Dozvoljeno samo ako je nalog prethodno zakazan.
     */
    @Transactional
    public WorkOrderResponse complete(Long id) {
        WorkOrder order = findOrder(id);

        if (order.getStatus() != WorkOrderStatus.SCHEDULED) {
            throw new BadRequestException("Samo zakazani nalog može se označiti kao završen.");
        }

        order.setStatus(WorkOrderStatus.COMPLETED);
        order.setCompletedAt(LocalDateTime.now());

        return WorkOrderResponse.from(workOrderRepository.save(order));
    }

    /**
     * Pomoćna metoda za dohvat naloga ili bacanje NotFoundException.
     */
    private WorkOrder findOrder(Long id) {
        return workOrderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Radni nalog nije pronađen."));
    }

    /**
     * Utility metoda za čišćenje tekstualnih polja.
     * Trim + null ako je prazno.
     */
    private String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}