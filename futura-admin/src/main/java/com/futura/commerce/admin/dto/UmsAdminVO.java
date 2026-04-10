package com.futura.commerce.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Admin profile view object
 *
 * @author Vitalii
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UmsAdminVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal price;
    private Long promotionQuota;
    private Long usedPromotionQuota;
}
