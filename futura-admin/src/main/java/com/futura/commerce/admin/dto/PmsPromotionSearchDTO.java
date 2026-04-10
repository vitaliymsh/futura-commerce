package com.futura.commerce.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Product detail search and save DTO
 *
 * @author Vitalii
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PmsPromotionSearchDTO extends PmsPromotionVO {

    private static final long serialVersionUID = 1L;

    private String description;
}
