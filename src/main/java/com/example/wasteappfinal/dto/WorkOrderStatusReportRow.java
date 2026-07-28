package com.example.wasteappfinal.dto;

import com.example.wasteappfinal.enums.WorkOrderStatus;

/**
 * DTO (record) predstavlja jedan redak izvješća o broju radnih naloga
 * grupiranih po statusu (CREATED, APPROVED, COMPLETED...).
 *
 * Koristi se u admin izvještajima i grafovima.
 */
public record WorkOrderStatusReportRow(
        WorkOrderStatus status,
        Long orderCount
) { }
