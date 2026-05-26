package com.futura.commerce.product.dto;

import lombok.Data;

import java.util.List;

/**
 * Category and product click tracking DTO
 *
 * @author Vitalii
 */
@Data
public class ClickDTO {
    private List<Click> clickList;
    private Long userId;

    @Data
    public static class Click {
        private Long productId;
        private String clickTime;
        private Long categoryId;
        private String categoryName;
        private String parentName;
    }
}
