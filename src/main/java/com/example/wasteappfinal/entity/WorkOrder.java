package com.example.wasteappfinal.entity;

import com.example.wasteappfinal.enums.QuantityUnit;
import com.example.wasteappfinal.enums.WorkOrderStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * JPA entitet predstavlja radni nalog.
 * Radni nalog je središnji poslovni objekt aplikacije i povezuje korisnika,
 * vrstu otpada, količinu, status i sve ključne datume procesa.
 * Adresa preuzimanja sprema se izravno u radni nalog.
 */
@Entity
@Table(name = "work_orders")
public class WorkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Korisnik koji je kreirao radni nalog.
     * ManyToOne jer jedan korisnik može imati više radnih naloga.
     * LAZY učitavanje radi performansi.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Vrsta otpada iz šifrarnika.
     * ManyToOne jer više naloga može koristiti istu vrstu otpada.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "waste_type_id", nullable = false)
    private WasteType wasteType;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal quantity;

    /** Jedinica mjere (KG, L, M3...) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private QuantityUnit unit;

    /** Status radnog naloga (CREATED, APPROVED, COMPLETED...) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WorkOrderStatus status;

    /** Adresa preuzimanja otpada */
    @Column(name = "pickup_address", nullable = false, length = 255)
    private String pickupAddress;

    /** Datum kada je korisnik zatražio nalog */
    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    /** Datum kada je nalog zakazan (postavlja admin) */
    @Column(name = "scheduled_for")
    private LocalDateTime scheduledFor;

    /** Datum kada je nalog dovršen */
    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    /** Dodatna napomena */
    @Column(length = 500)
    private String note;

    /** Datum kreiranja zapisa u bazi */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Konstruktor postavlja početne vrijednosti:
     * - status = CREATED
     * - requestedAt = sada
     * - createdAt = sada
     * Hibernate zahtijeva prazan konstruktor.
     */
    public WorkOrder() {
        LocalDateTime now = LocalDateTime.now();
        this.status = WorkOrderStatus.CREATED;
        this.requestedAt = now;
        this.createdAt = now;
    }

    // getteri i setteri
    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public WasteType getWasteType() { return wasteType; }
    public void setWasteType(WasteType wasteType) { this.wasteType = wasteType; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public QuantityUnit getUnit() { return unit; }
    public void setUnit(QuantityUnit unit) { this.unit = unit; }

    public WorkOrderStatus getStatus() { return status; }
    public void setStatus(WorkOrderStatus status) { this.status = status; }

    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }

    public LocalDateTime getRequestedAt() { return requestedAt; }

    public LocalDateTime getScheduledFor() { return scheduledFor; }
    public void setScheduledFor(LocalDateTime scheduledFor) { this.scheduledFor = scheduledFor; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
