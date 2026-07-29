package com.example.wasteappfinal.service;

import com.example.wasteappfinal.dto.GeneratedPdf;
import com.example.wasteappfinal.dto.WasteManifestCreateRequest;
import com.example.wasteappfinal.dto.WasteManifestResponse;
import com.example.wasteappfinal.entity.*;
import com.example.wasteappfinal.enums.WorkOrderStatus;
import com.example.wasteappfinal.exception.BadRequestException;
import com.example.wasteappfinal.exception.NotFoundException;
import com.example.wasteappfinal.repository.UserRepository;
import com.example.wasteappfinal.repository.WasteManifestRepository;
import com.example.wasteappfinal.repository.WorkOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/** Poslovna logika za prateće listove. */
@Service
public class WasteManifestService {
    private final WasteManifestRepository manifestRepository;
    private final WorkOrderRepository workOrderRepository;
    private final UserRepository userRepository;
    private final WasteManifestPdfService pdfService;

    public WasteManifestService(
            WasteManifestRepository manifestRepository,
            WorkOrderRepository workOrderRepository,
            UserRepository userRepository,
            WasteManifestPdfService pdfService
    ) {
        this.manifestRepository = manifestRepository;
        this.workOrderRepository = workOrderRepository;
        this.userRepository = userRepository;
        this.pdfService = pdfService;
    }

    @Transactional
    public WasteManifestResponse create(WasteManifestCreateRequest request) {
        WorkOrder order = workOrderRepository.findById(request.getWorkOrderId())
                .orElseThrow(() -> new NotFoundException("Radni nalog nije pronađen."));
        if (manifestRepository.existsByWorkOrderId(order.getId())) {
            throw new BadRequestException("Za taj radni nalog već postoji prateći list.");
        }
        if (order.getStatus() != WorkOrderStatus.SCHEDULED && order.getStatus() != WorkOrderStatus.COMPLETED) {
            throw new BadRequestException("Prateći list može se izraditi za zakazani ili završeni nalog.");
        }

        WasteManifest manifest = new WasteManifest();
        manifest.setWorkOrder(order);
        manifest.setNote(clean(request.getNote()));
        manifest.setManifestNumber("PL-" + LocalDateTime.now().getYear() + "-" + String.format("%06d", order.getId()));
        return WasteManifestResponse.from(manifestRepository.save(manifest));
    }

    @Transactional(readOnly = true)
    public List<WasteManifestResponse> listAll() {
        return manifestRepository.findAllByOrderByIssuedAtDesc().stream()
                .map(WasteManifestResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<WasteManifestResponse> listMine(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new NotFoundException("Korisnik nije pronađen."));
        return manifestRepository.findAllByWorkOrderUserIdOrderByIssuedAtDesc(user.getId()).stream()
                .map(WasteManifestResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public WasteManifestResponse getAccessible(Long id, String email, boolean admin) {
        return WasteManifestResponse.from(findAccessibleEntity(id, email, admin));
    }

    @Transactional(readOnly = true)
    public GeneratedPdf generatePdf(Long id, String email, boolean admin) {
        WasteManifest manifest = findAccessibleEntity(id, email, admin);
        return new GeneratedPdf(manifest.getManifestNumber() + ".pdf", pdfService.generate(manifest));
    }

    private WasteManifest findAccessibleEntity(Long id, String email, boolean admin) {
        if (admin) {
            return manifestRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Prateći list nije pronađen."));
        }
        return manifestRepository.findByIdAndWorkOrderUserEmailIgnoreCase(id, email)
                .orElseThrow(() -> new NotFoundException("Prateći list nije pronađen ili mu nemate pristup."));
    }

    private String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
