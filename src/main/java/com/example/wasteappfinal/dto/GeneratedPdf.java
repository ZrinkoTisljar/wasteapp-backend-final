package com.example.wasteappfinal.dto;

/**
 * DTO koji predstavlja generirani PDF.
 *
 * Ovaj record vraća:
 * - naziv datoteke (fileName)
 * - sadržaj PDF-a kao byte[] (content)
 *
 * Ideja: PDF sadržaj i naziv vraćaju se zajedno kako bi se baza dohvatila samo jednom.
 * Record automatski generira konstruktor, equals, hashCode i toString metode.
 */
public record GeneratedPdf(
        String fileName,   // Naziv PDF datoteke, npr. "report.pdf"
        byte[] content     // Sadržaj PDF-a u bajtovima
) { }
