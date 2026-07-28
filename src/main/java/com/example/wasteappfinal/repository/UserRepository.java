package com.example.wasteappfinal.repository;

import com.example.wasteappfinal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repozitorij za entitet User.
 * Framework automatski generira implementaciju na temelju naziva metoda.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Pronalaženje korisnika po email adresi (neovisno o velikim/malim slovima).
     * Vraća Optional jer korisnik možda ne postoji.
     */
    Optional<User> findByEmailIgnoreCase(String email);

    /**
     * Provjerava postoji li korisnik s danim emailom.
     * Koristi se pri registraciji kako bi se spriječilo dupliranje.
     */
    boolean existsByEmailIgnoreCase(String email);

    /**
     * Vraća sve korisnike sortirane po datumu kreiranja (najnoviji prvi).
     */
    List<User> findAllByOrderByCreatedAtDesc();

    /**
     * Vraća sve korisnike koji još nisu odobreni.
     * Sortira ih po datumu kreiranja (najstariji zahtjev prvi).
     */
    List<User> findByApprovedFalseOrderByCreatedAtAsc();
}
