package com.example.wasteappfinal.dto;

import com.example.wasteappfinal.enums.QuantityUnit;
import java.math.BigDecimal;

/**
 * DTO (record) predstavlja jedan redak izvješća o ukupnoj količini otpada
 * grupiranoj po vrsti otpada i mjernoj jedinici.
 *
 * Record automatski generira:
 * - final polja
 * - konstruktor
 * - gettere
 * - equals/hashCode
 * - toString
 * bez dodatnog koda
 *
 * Idealno za read-only podatke iz JPA projekcija.
 */
public record WasteTypeReportRow(
        String wasteTypeCode,
        String wasteTypeName,
        QuantityUnit unit,
        BigDecimal totalQuantity
) { }
