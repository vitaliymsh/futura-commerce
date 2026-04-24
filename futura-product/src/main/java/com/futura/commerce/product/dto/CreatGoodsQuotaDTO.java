package com.futura.commerce.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Product promotion quota allocation DTO
 *
 * @author Vitalii
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatGoodsQuotaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long quota;
}
