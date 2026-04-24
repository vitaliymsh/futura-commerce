package com.futura.commerce.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Category tree node DTO
 *
 * @author Vitalii
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryNode implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private Long parentId;
    private Integer sort;
    private String icon;
    private BigDecimal price;
    private List<CategoryNode> children = new ArrayList<>();
}
