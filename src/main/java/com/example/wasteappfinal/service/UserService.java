package com.example.wasteappfinal.service;

import com.example.wasteappfinal.dto.MessageResponse;
import com.example.wasteappfinal.dto.UserResponse;
import com.example.wasteappfinal.enums.Role;
import com.example.wasteappfinal.exception.BadRequestException;
import com.example.wasteappfinal.exception.NotFoundException;
import com.example.wasteappfinal.repository.UserRepository;
import com.example.wasteappfinal.repository.WorkOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servisni sloj koji sadrži administratorsku poslovnu logiku vezanu uz korisnike.
 *
 * Ovdje se nalaze operacije koje administrator može izvršavati nad korisničkim računima:
 * - pregled svih korisnika
 * - pregled korisnika koji čekaju odobrenje
 * - odobravanje korisnika
 * - brisanje korisnika uz provjere poslovnih pravila
 *
 * Servis koristi custom iznimke (NotFoundException, BadRequestException) kako bi
 * se osigurala dosljedna obrada pogrešaka u REST API-ju.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final WorkOrderRepository workOrderRepository;

    /**
     * Konstruktor s dependency injectionom repozitorija.
     */
    public UserService(UserRepository userRepository, WorkOrderRepository workOrderRepository) {
        this.userRepository = userRepository;
        this.workOrderRepository = workOrderRepository;
    }

    /**
     * Vraća listu svih korisnika, sortiranu po datumu kreiranja (najnoviji prvi).
     */
    public List<UserResponse> listAll() {
        return userRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(UserResponse::from)
                .toList();
    }

    /**
     * Vraća korisnike koji još nisu odobreni, sortirane po datumu kreiranja (najstariji prvi).
     */
    public List<UserResponse> listPending() {
        return userRepository.findByApprovedFalseOrderByCreatedAtAsc()
                .stream()
                .map(UserResponse::from)
                .toList();
    }

    /**
     * Odobrava korisnika. Ako korisnik ne postoji, baca NotFoundException.
     */
    @Transactional
    public UserResponse approve(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Korisnik nije pronađen."));

        user.setApproved(true);
        return UserResponse.from(userRepository.save(user));
    }

    /**
     * Briše korisnika uz provjeru poslovnih pravila:
     * - administratorski račun se ne može obrisati
     * - korisnik koji ima radne naloge ne može se obrisati
     */
    @Transactional
    public MessageResponse delete(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Korisnik nije pronađen."));

        if (user.getRole() == Role.ADMIN) {
            throw new BadRequestException("Administratorski račun ne može se obrisati ovom funkcijom.");
        }

        if (workOrderRepository.existsByUserId(id)) {
            throw new BadRequestException("Korisnik ima radne naloge pa ga nije moguće obrisati.");
        }

        userRepository.delete(user);
        return new MessageResponse("Korisnik je obrisan.");
    }
}
