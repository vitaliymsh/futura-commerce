package com.futura.commerce.admin.dto;

import lombok.Data;

import java.util.List;

/**
 * Batch export request DTO
 *
 * @author Vitalii
 */
@Data
public class BatchExportDTO {
    /**
     * List of order IDs to export
     */
    private List<Long> ids;
}
