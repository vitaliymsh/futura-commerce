package com.futura.commerce.search.repository;

import com.futura.commerce.mbg.model.PmsProduct;
import com.futura.commerce.mbg.repository.PmsProductCategoryRepository;
import com.futura.commerce.mbg.repository.PmsProductRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Product search repository using Spring Data JPA
 *
 * @author Vitalii
 */
@Repository
public class ProductSearchRepository {

    @Resource
    private PmsProductRepository productRepository;

    @Resource
    private PmsProductCategoryRepository productCategoryRepository;

    /**
     * Retrieve all products enriched with category name for search indexing
     */
    public List<PmsProduct> listAllProducts() {
        List<PmsProduct> products = productRepository.findAll();
        if (products.isEmpty()) {
            return List.of();
        }

        List<Long> categoryIds = products.stream()
                .map(PmsProduct::getCategoryId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<Long, String> categoryNameMap = productCategoryRepository.findAllById(categoryIds).stream()
                .collect(Collectors.toMap(c -> c.getId(), c -> c.getName() != null ? c.getName() : "", (k1, k2) -> k1));

        for (PmsProduct p : products) {
            if (p.getCategoryId() != null && categoryNameMap.containsKey(p.getCategoryId())) {
                p.setCategoryName(categoryNameMap.get(p.getCategoryId()));
            }
        }
        return products;
    }
}
