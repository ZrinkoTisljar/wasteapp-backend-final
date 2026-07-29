package com.example.wasteappfinal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Globalna konfiguracija za PasswordEncoder.
 *
 * Spring Security 6 više ne kreira automatski PasswordEncoder,
 * pa ga moramo definirati ručno kao @Bean.
 *
 * Ovaj bean omogućuje:
 * - hashiranje lozinki prilikom registracije
 * - provjeru lozinki prilikom prijave
 * - korištenje BCrypt algoritma (industrijski standard)
 */
@Configuration
public class PasswordConfig {

    /**
     * Registrira BCryptPasswordEncoder kao Spring bean.
     * AuthService ga automatski dobiva kroz konstruktor.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
