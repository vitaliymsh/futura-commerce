package com.futura.commerce.admin.dto;

import lombok.Data;

/**
 * Tracking number update DTO
 *
 * @author Vitalii
 */
@Data
public class UpdateTrackingNoDTO {
    private String oldTrackingNo;
    private String newTrackingNo;
    private String operator;
    private String reason;
}
