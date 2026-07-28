package com.example.wasteappfinal.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/** JPA entitet WasteType predstavlja vrstu otpada iz šifrarnika i mapira se na tablicu waste_types u bazi podataka.
 *  Svaki zapis definira jednu vrstu otpada koja se koristi u radnim nalozima i pratećim listovima.
 * */
@Entity
@Table(name = "waste_types")
public class WasteType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Jedinstveni kod vrste otpada (npr. PLASTIC, PAPER, GLASS) */
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    /** Naziv vrste otpada (prikazuje se korisniku u aplikaciji) */
    @Column(nullable = false, length = 255)
    private String name;

    /** Opis vrste otpada (nije obavezan) */
    @Column(length = 500)
    private String description;

    /** Status aktivacije vrste otpada (1 = aktivno, 0 = neaktivno) */
    @Column(name = "is_active", nullable = false)
    private boolean active;

    /** Datum kreiranja zapisa (postavlja se automatski) */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Konstruktor koji automatski postavlja početne vrijednosti:
     * - active = true (svaka vrsta otpada je aktivna pri kreiranju)
     * - createdAt = trenutni datum i vrijeme
     * Hibernate zahtijeva prazan konstruktor.
     */
    public WasteType() {
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    //GETTERI I SETTERI

    /** Vraća ID vrste otpada */
    public Long getId() { return id; }

    /** Postavlja kod i vraća naziv vrste otpada*/
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
