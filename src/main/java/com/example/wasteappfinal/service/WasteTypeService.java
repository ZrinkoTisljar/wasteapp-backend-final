package com.example.wasteappfinal.service;

import com.example.wasteappfinal.dto.*;
import com.example.wasteappfinal.entity.WasteType;
import com.example.wasteappfinal.exception.BadRequestException;
import com.example.wasteappfinal.exception.NotFoundException;
import com.example.wasteappfinal.repository.WasteTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Poslovna logika šifrarnika vrsta otpada. */
@Service
public class WasteTypeService {
    private final WasteTypeRepository repository;

    public WasteTypeService(WasteTypeRepository repository) {
        this.repository = repository;
    }

    public List<WasteTypeResponse> listActive() {
        return repository.findAllByActiveTrueOrderByNameAsc().stream().map(WasteTypeResponse::from).toList();
    }

    public List<WasteTypeResponse> listAll() {
        return repository.findAllByOrderByNameAsc().stream().map(WasteTypeResponse::from).toList();
    }

    @Transactional
    public WasteTypeResponse create(WasteTypeCreateRequest request) {
        String code = request.getCode().trim().toUpperCase();
        if (repository.existsByCodeIgnoreCase(code)) {
            throw new BadRequestException("Vrsta otpada s tim kodom već postoji.");
        }
        WasteType type = new WasteType();
        type.setCode(code);
        type.setName(request.getName().trim());
        type.setDescription(clean(request.getDescription()));
        return WasteTypeResponse.from(repository.save(type));
    }

    @Transactional
    public WasteTypeResponse update(Long id, WasteTypeUpdateRequest request) {
        WasteType type = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vrsta otpada nije pronađena."));
        type.setName(request.getName().trim());
        type.setDescription(clean(request.getDescription()));
        type.setActive(request.isActive());
        return WasteTypeResponse.from(repository.save(type));
    }

    @Transactional
    public MessageResponse deactivate(Long id) {
        WasteType type = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vrsta otpada nije pronađena."));
        type.setActive(false);
        repository.save(type);
        return new MessageResponse("Vrsta otpada je deaktivirana.");
    }

    private String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
