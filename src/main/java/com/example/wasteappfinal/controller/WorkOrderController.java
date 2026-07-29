package com.example.wasteappfinal.controller;

import com.example.wasteappfinal.dto.WorkOrderCreateRequest;
import com.example.wasteappfinal.dto.WorkOrderResponse;
import com.example.wasteappfinal.service.WorkOrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderController {
    private final WorkOrderService service;

    public WorkOrderController(WorkOrderService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<WorkOrderResponse> create(
            Authentication authentication,
            @Valid @RequestBody WorkOrderCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createForUser(authentication.getName(), request));
    }

    @GetMapping("/mine")
    public List<WorkOrderResponse> mine(Authentication authentication) {
        return service.listMine(authentication.getName());
    }
}
