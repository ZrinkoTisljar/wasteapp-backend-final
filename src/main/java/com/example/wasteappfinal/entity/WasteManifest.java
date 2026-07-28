package com.example.wasteappfinal.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/** Prateći list nastaje iz jednog radnog naloga. */
@Entity
@Table(name = "waste_manifests")
public class WasteManifest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Jedinstveni broj prateceg lista.
     * Primjer: WM-2026-000001
     * */
    @Column(name = "manifest_number", nullable = false, unique = true, length = 50)
    private String manifestNumber;

    /**
     * Veza na radni nalog iz kojeg je nastao prateci list
     * Jedan work order -> jedan manifest
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "work_order_id", nullable = false, unique = true)
    private WorkOrder workOrder;

    /**
     * Datum izdavanja pratecek lista
     */
    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    /**
     * Dodatna napomena
     */
    @Column(length = 500)
    private String note;

    /** Datum kreiranja zapisa u bazi */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**  Konstruktor automatski postavlja:
     * - issuedAt = trenutni datum i vrijeme
     * - createdAt = trenutni datum i vrijeme
    */
    public WasteManifest() {
        LocalDateTime now = LocalDateTime.now();
        this.issuedAt = now;
        this.createdAt = now;
    }

    // GETTERI I SETTERI
    public Long getId() { return id; }

    public String getManifestNumber() { return manifestNumber; }
    public void setManifestNumber(String manifestNumber) { this.manifestNumber = manifestNumber; }

    public WorkOrder getWorkOrder() { return workOrder; }
    public void setWorkOrder(WorkOrder workOrder) { this.workOrder = workOrder; }

    public LocalDateTime getIssuedAt() { return issuedAt; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
