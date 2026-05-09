package com.futura.commerce.product.service.impl;

import com.futura.commerce.mbg.model.ProductParams;
import com.futura.commerce.mbg.repository.ProductParamsRepository;
import com.futura.commerce.product.service.ProductParamsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing ProductParams
 *
 * @author Vitalii
 */
@Service
public class ProductParamsServiceImpl implements ProductParamsService {

    @Resource
    private ProductParamsRepository productParamsRepository;

    @Override
    public List<ProductParams> findByProductsId(Long productsId) {
        return productParamsRepository.findByProductsId(productsId);
    }

    @Override
    public ProductParams save(ProductParams productParams) {
        return productParamsRepository.save(productParams);
    }

    @Override
    public void deleteById(Long id) {
        productParamsRepository.deleteById(id);
    }
}
