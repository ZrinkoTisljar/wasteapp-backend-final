package com.example.wasteappfinal.service;

import com.example.wasteappfinal.dto.WasteTypeReportRow;
import com.example.wasteappfinal.dto.WorkOrderStatusReportRow;
import com.example.wasteappfinal.repository.WorkOrderRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Servisni sloj za administratorska izvješća.
 * Ovdje se nalaze metode koje dohvaćaju agregirane podatke
 * iz repozitorija i vraćaju ih kontroleru.
 */
@Service
public class ReportService {

    private final WorkOrderRepository repository;

    /**
     * Konstruktor s dependency injectionom.
     * Spring automatski ubacuje implementaciju WorkOrderRepository-a.
     */
    public ReportService(WorkOrderRepository repository) {
        this.repository = repository;
    }

    /**
     * Izvještaj: ukupna količina otpada po vrsti.
     * Delegira poziv repozitoriju koji izvršava agregacijski upit.
     *
     * @return lista DTO objekata WasteTypeReportRow
     */
    public List<WasteTypeReportRow> wasteByType() {
        return repository.reportTotalWasteByType();
    }

    /**
     * Izvještaj: broj radnih naloga po statusu.
     * Delegira poziv repozitoriju koji izvršava GROUP BY upit.
     *
     * @return lista DTO objekata WorkOrderStatusReportRow
     */
    public List<WorkOrderStatusReportRow> workOrdersByStatus() {
        return repository.reportCountByStatus();
    }
}
