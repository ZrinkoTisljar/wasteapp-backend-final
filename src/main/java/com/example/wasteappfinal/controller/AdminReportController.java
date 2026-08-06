package com.example.wasteappfinal.controller;

import com.example.wasteappfinal.dto.WasteTypeReportRow;
import com.example.wasteappfinal.dto.WorkOrderStatusReportRow;
import com.example.wasteappfinal.service.ReportService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST kontroler za administratorske izvještaje.
 * Izlaže API rute koje vraćaju agregirane podatke o otpadu i radnim nalozima.
 */
@RestController
@RequestMapping("/api/admin/reports")
public class AdminReportController {

    private final ReportService service;

    /**
     * Konstruktor s dependency injectionom.
     * Spring automatski ubacuje implementaciju ReportService-a.
     */
    public AdminReportController(ReportService service) {
        this.service = service;
    }

    /**
     * Izvještaj: ukupna količina otpada po vrsti.
     * GET /api/admin/reports/waste-by-type
     *
     * Vraća listu DTO objekata WasteTypeReportRow.
     */
    @GetMapping("/waste-by-type")
    public List<WasteTypeReportRow> wasteByType() {
        return service.wasteByType();
    }

    /**
     * Izvještaj: broj radnih naloga po statusu.
     * GET /api/admin/reports/work-orders-by-status
     *
     * Vraća listu DTO objekata WorkOrderStatusReportRow.
     */
    @GetMapping("/work-orders-by-status")
    public List<WorkOrderStatusReportRow> workOrdersByStatus() {
        return service.workOrdersByStatus();
    }
}
