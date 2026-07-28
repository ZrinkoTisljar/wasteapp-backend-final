package com.example.wasteappfinal.dto;

import com.example.wasteappfinal.enums.UserType;
import jakarta.validation.constraints.*;

/**
 * DTO koji predstavlja podatke koje korisnik šalje prilikom registracije.
 *
 * Sadrži validacijske anotacije koje osiguravaju:
 * - ispravnost e‑pošte
 * - minimalnu duljinu lozinke
 * - obvezan odabir tipa korisnika (građanin / tvrtka)
 * - ispravnost OIB-a (11 znamenki ili prazno)
 * - ograničenja duljine za adresu i telefon
 *
 * DTO se koristi u kontroleru za registraciju, gdje se validira prije
 * prosljeđivanja servisnom sloju.
 */
public class RegisterRequest {

    /** Email korisnika – mora biti ispravan format i ne smije biti prazan. */
    @NotBlank(message = "Adresa e-pošte je obvezna.")
    @Email(message = "Adresa e-pošte nije ispravna.")
    private String email;

    /** Lozinka – obvezna, minimalno 8 znakova, maksimalno 72 (BCrypt limit). */
    @NotBlank(message = "Lozinka je obvezna.")
    @Size(min = 8, max = 72, message = "Lozinka mora imati između 8 i 72 znaka.")
    private String password;

    /** Tip korisnika – građanin ili tvrtka. Obvezno polje. */
    @NotNull(message = "Tip korisnika je obvezan.")
    private UserType userType;

    /** Puno ime građanina (ako je userType = CITIZEN). */
    private String fullName;

    /** Naziv tvrtke (ako je userType = COMPANY). */
    private String companyName;

    /** OIB – mora imati 11 znamenki ili biti prazan. */
    @Pattern(regexp = "^$|^[0-9]{11}$", message = "OIB mora sadržavati 11 znamenki.")
    private String oib;

    /** Adresa korisnika – obvezna, maksimalno 255 znakova. */
    @NotBlank(message = "Adresa je obvezna.")
    @Size(max = 255, message = "Adresa smije imati najviše 255 znakova.")
    private String address;

    /** Telefonski broj – nije obvezan, ali ima ograničenje duljine. */
    @Size(max = 64, message = "Telefon smije imati najviše 64 znaka.")
    private String phone;

    // --- GETTERI I SETTERI ---

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

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
}
