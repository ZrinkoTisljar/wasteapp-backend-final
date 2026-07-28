package com.example.wasteappfinal.entity;

import com.example.wasteappfinal.enums.Role;
import com.example.wasteappfinal.enums.UserType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entitet predstavlja korisnika i mapira se na tablicu users.
 * Lozinka se sprema samo kao BCrypt sažetak u stupcu password_hash.
 */
@Entity
@Table(name = "users")
public class User {

    /** Primarni ključ (AUTO_INCREMENT) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Email korisnika mora biti jedinstven i obavezan */
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    /** BCrypt hash lozinke (nikad se ne sprema plain-text) */
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    /** Uloga korisnika: ADMIN ili USER */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    /** Tip korisnika: CITIZEN ili COMPANY */
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false, length = 20)
    private UserType userType;

    /** Ime i prezime građanina (ako je user_type = CITIZEN) */
    @Column(name = "full_name", length = 255)
    private String fullName;

    /** Naziv tvrtke (ako je user_type = COMPANY) */
    @Column(name = "company_name", length = 255)
    private String companyName;

    @Column(length = 32)
    private String oib;

    /** Adresa korisnika (obavezno polje) */
    @Column(nullable = false, length = 255)
    private String address;

    @Column(length = 64)
    private String phone;

    /** Status odobrenja korisnika od strane administratora */
    @Column(name = "is_approved", nullable = false)
    private boolean approved= false;

    /** Datum kreiranja korisnika (postavlja se automatski) */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**konstruktor koji automatski postavi createdAt kod kreiranja objekta (prije spremanja u bazu)
     * Hibernate zahtjeva prazan konstruktor
     */
    public User() {
        this.createdAt = LocalDateTime.now();
       // this.approved = false;
    }

    /** Vraća ime građanina ili naziv tvrtke za prikaz u aplikaciji. */
    public String getDisplayName() {
        if (fullName != null && !fullName.isBlank()) {
            return fullName;
        }
        return companyName;
    }

    // / --- GETTERI/SETTERI ---

    public Long getId() { return id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getOib() { return oib; }
    public void setOib(String oib) { this.oib = oib; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
