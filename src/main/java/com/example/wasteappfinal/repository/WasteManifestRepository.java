package com.example.wasteappfinal.repository;

import com.example.wasteappfinal.entity.WasteManifest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repozitorij za entitet WasteManifest.
 * Sadrži metode za dohvat pratećih listova, provjere postojanja
 * i učitavanje povezanih entiteta (WorkOrder, User, WasteType).
 */
public interface WasteManifestRepository extends JpaRepository<WasteManifest, Long> {

    /**
     * Provjerava postoji li prateći list za određeni radni nalog.
     * Koristi se kako bi se spriječilo kreiranje duplikata.
     */
    boolean existsByWorkOrderId(Long workOrderId);

    /**
     * Vraća sve prateće listove sortirane po datumu izdavanja (najnoviji prvi).
     * EntityGraph učitava povezane entitete u jednom upitu:
     * - workOrder
     * - workOrder.user
     * - workOrder.wasteType
     */
    @EntityGraph(attributePaths = {"workOrder", "workOrder.user", "workOrder.wasteType"})
    List<WasteManifest> findAllByOrderByIssuedAtDesc();

    /**
     * Vraća sve prateće listove određenog korisnika.
     * Sortira po datumu izdavanja (najnoviji prvi).
     */
    @EntityGraph(attributePaths = {"workOrder", "workOrder.user", "workOrder.wasteType"})
    List<WasteManifest> findAllByWorkOrderUserIdOrderByIssuedAtDesc(Long userId);

    /**
     * Dohvaća prateći list po ID-u, zajedno s povezanim entitetima.
     * EntityGraph sprječava lazy loading probleme u kontroleru.
     */
    @EntityGraph(attributePaths = {"workOrder", "workOrder.user", "workOrder.wasteType"})
    Optional<WasteManifest> findById(Long id);

    /**
     * Dohvaća prateći list samo ako pripada korisniku s određenim emailom.
     * Koristi se za sigurnosne provjere (user može vidjeti samo svoje dokumente).
     */
    @EntityGraph(attributePaths = {"workOrder", "workOrder.user", "workOrder.wasteType"})
    Optional<WasteManifest> findByIdAndWorkOrderUserEmailIgnoreCase(Long id, String email);
}
