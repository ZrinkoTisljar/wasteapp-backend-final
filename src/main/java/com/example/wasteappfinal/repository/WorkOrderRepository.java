package com.example.wasteappfinal.repository;

import com.example.wasteappfinal.dto.WasteTypeReportRow;
import com.example.wasteappfinal.dto.WorkOrderStatusReportRow;
import com.example.wasteappfinal.entity.WorkOrder;
import com.example.wasteappfinal.enums.WorkOrderStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repozitorij za radne naloge.
 * Sadrži metode za dohvat naloga, filtriranje i generiranje izvještaja.
 */
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {

    /**
     * Vraća sve radne naloge određenog korisnika.
     * EntityGraph učitava povezane entitete (user, wasteType) u jednom upitu.
     */
    @EntityGraph(attributePaths = {"user", "wasteType"})
    List<WorkOrder> findAllByUserIdOrderByRequestedAtDesc(Long userId);

    /**
     * Vraća sve radne naloge sortirane po datumu zahtjeva.
     */
    @EntityGraph(attributePaths = {"user", "wasteType"})
    List<WorkOrder> findAllByOrderByRequestedAtDesc();

    /**
     * Provjerava ima li korisnik barem jedan radni nalog.
     */
    boolean existsByUserId(Long userId);


    /**
     * Dinamičko filtriranje za admin panel.
     * Svi parametri su opcionalni.
     */
    @EntityGraph(attributePaths = {"user", "wasteType"})
    @Query("""
        SELECT wo FROM WorkOrder wo
        WHERE (:status IS NULL OR wo.status = :status)
          AND (:wasteTypeCode IS NULL OR :wasteTypeCode = '' OR wo.wasteType.code = :wasteTypeCode)
          AND (:userEmail IS NULL OR :userEmail = '' OR LOWER(wo.user.email) LIKE LOWER(CONCAT('%', :userEmail, '%')))
          AND (:pickupAddress IS NULL OR :pickupAddress = '' OR LOWER(wo.pickupAddress) LIKE LOWER(CONCAT('%', :pickupAddress, '%')))
        ORDER BY wo.requestedAt DESC
    """)
    List<WorkOrder> filterAdmin(
            @Param("status") WorkOrderStatus status,
            @Param("wasteTypeCode") String wasteTypeCode,
            @Param("userEmail") String userEmail,
            @Param("pickupAddress") String pickupAddress
    );

    /**
     * Izvještaj: ukupna količina otpada po vrsti.
     * Koristi DTO WasteTypeReportRow.
     */
    @Query("""
        SELECT new com.example.wasteapp.dto.WasteTypeReportRow(
            wo.wasteType.code,
            wo.wasteType.name,
            wo.unit,
            SUM(wo.quantity)
        )
        FROM WorkOrder wo
        WHERE wo.status = com.example.wasteapp.entity.WorkOrderStatus.COMPLETED
        GROUP BY wo.wasteType.code, wo.wasteType.name, wo.unit
        ORDER BY wo.wasteType.name, wo.unit
    """)
    List<WasteTypeReportRow> reportTotalWasteByType();

    /**
     * Izvještaj: broj radnih naloga po statusu.
     */
    @Query("""
        SELECT new com.example.wasteapp.dto.WorkOrderStatusReportRow(wo.status, COUNT(wo))
        FROM WorkOrder wo
        GROUP BY wo.status
        ORDER BY wo.status
    """)
    List<WorkOrderStatusReportRow> reportCountByStatus();
}
