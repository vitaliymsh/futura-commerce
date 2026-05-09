package com.futura.commerce.product.service;

import com.futura.commerce.mbg.model.ProductParams;

import java.util.List;

/**
 * Service interface for managing ProductParams
 *
 * @author Vitalii
 */
public interface ProductParamsService {

    List<ProductParams> findByProductsId(Long productsId);

    ProductParams save(ProductParams productParams);

    void deleteById(Long id);
}
