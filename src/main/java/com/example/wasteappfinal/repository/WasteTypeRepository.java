package com.example.wasteappfinal.repository;

import com.example.wasteappfinal.entity.WasteType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Spring Data JPA repozitorij za entitet WasteType.
 * Framework automatski generira implementaciju i SQL upite na temelju naziva metoda.
 */
public interface WasteTypeRepository extends JpaRepository<WasteType, Long> {

    /**
     * Provjerava postoji li vrsta otpada s danim kodom (case-insensitive).
     * Koristi se pri dodavanju novih vrsta otpada kako bi se spriječilo dupliranje.
     */
    boolean existsByCodeIgnoreCase(String code);

    /**
     * Vraća sve aktivne vrste otpada sortirane po nazivu (A–Z).
     * Koristi se u formama gdje korisnik bira vrstu otpada.
     */
    List<WasteType> findAllByActiveTrueOrderByNameAsc();

    /**
     * Vraća sve vrste otpada sortirane po nazivu (A–Z), bez obzira na status.
     */
    List<WasteType> findAllByOrderByNameAsc();
}
