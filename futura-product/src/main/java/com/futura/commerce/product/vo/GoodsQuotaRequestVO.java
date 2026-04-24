package com.futura.commerce.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Request payload for creating goods promotion quota
 *
 * @author Vitalii
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsQuotaRequestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<CreatGoodsQuotaDTO> goods;
    private Long packageId;
}
