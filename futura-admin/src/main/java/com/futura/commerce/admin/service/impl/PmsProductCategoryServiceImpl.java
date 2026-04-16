package com.futura.commerce.admin.service.impl;

import com.futura.commerce.admin.dto.CategoryNode;
import com.futura.commerce.admin.service.PmsProductCategoryService;
import com.futura.commerce.admin.service.PmsProductService;
import com.futura.commerce.common.baseCommon.CommonResult;
import com.futura.commerce.mbg.model.PmsProduct;
import com.futura.commerce.mbg.model.PmsProductCategory;
import com.futura.commerce.mbg.repository.PmsProductCategoryRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service implementation for managing PmsProductCategory
 *
 * @author Vitalii
 */
@Slf4j
@Service
public class PmsProductCategoryServiceImpl implements PmsProductCategoryService {

    @Resource
    private PmsProductCategoryRepository pmsProductCategoryRepository;

    @Resource
    private PmsProductService productService;

    @Override
    public List<PmsProductCategory> findAll() {
        return pmsProductCategoryRepository.findAll();
    }

    @Override
    public Optional<PmsProductCategory> findById(Long id) {
        return pmsProductCategoryRepository.findById(id);
    }

    @Override
    public PmsProductCategory save(PmsProductCategory entity) {
        return pmsProductCategoryRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        pmsProductCategoryRepository.deleteById(id);
    }

    @Override
    public CommonResult<String> addCategory(PmsProductCategory category) {
        if (category == null) {
            return CommonResult.failed("Category cannot be null");
        }
        pmsProductCategoryRepository.save(category);
        return CommonResult.success("Category created successfully");
    }

    @Override
    public CommonResult<String> updateCategory(Long id, PmsProductCategory category) {
        if (id == null || category == null) {
            return CommonResult.failed("Invalid parameters");
        }
        Optional<PmsProductCategory> existing = pmsProductCategoryRepository.findById(id);
        if (existing.isEmpty()) {
            return CommonResult.failed("Category not found");
        }
        category.setId(id);
        pmsProductCategoryRepository.save(category);
        return CommonResult.success("Category updated successfully");
    }

    @Override
    public CommonResult<String> deleteCategory(Long id) {
        if (id == null) {
            return CommonResult.failed("Invalid category ID");
        }
        Optional<PmsProductCategory> categoryOpt = pmsProductCategoryRepository.findById(id);
        if (categoryOpt.isEmpty()) {
            return CommonResult.failed("Category not found");
        }

        PmsProductCategory category = categoryOpt.get();
        if (category.getParentId() != null && category.getParentId() == 0) {
            // Level 1 category: check for level 2 child categories
            long count = pmsProductCategoryRepository.countByParentId(id);
            if (count > 0) {
                return CommonResult.failed("Cannot delete category with existing subcategories");
            }
        } else {
            // Level 2 category: check for assigned products
            long count = pmsProductRepository.countByCategoryId(id);
            if (count > 0) {
                return CommonResult.failed("Cannot delete category with associated products");
            }
        }

        pmsProductCategoryRepository.deleteById(id);
        return CommonResult.success("Category deleted successfully");
    }

    @Override
    public CommonResult<List<CategoryNode>> getCategoriesList() {
        // 1. fetch all category levels
        List<PmsProductCategory> categoryList = pmsProductCategoryRepository.findAll();

        // 2. fetch all products (acting as level 3 in this tree view)
        List<PmsProduct> productList = productService.findAll();

        // 3. construct level 1 -> level 2 -> product tree
        List<CategoryNode> treeList = buildCategoryTree(categoryList, productList);

        return CommonResult.success(treeList, "Category tree fetched successfully");
    }

    /**
     * Build 3-level tree:
     * Level 1 category -> Level 2 category -> Level 3 Product items
     */
    private List<CategoryNode> buildCategoryTree(
            List<PmsProductCategory> categoryList,
            List<PmsProduct> productList
    ) {
        // lookup map: category id -> node
        Map<Long, CategoryNode> nodeMap = new HashMap<>();
        List<CategoryNode> rootList = new ArrayList<>();

        // step 1: initialize category nodes
        for (PmsProductCategory category : categoryList) {
            CategoryNode node = new CategoryNode();
            node.setId(category.getId());
            node.setName(category.getName());
            node.setParentId(category.getParentId());
            node.setSort(category.getSort());
            node.setIcon(category.getIcon());
            node.setChildren(new ArrayList<>());

            nodeMap.put(node.getId(), node);
        }

        // step 2: assemble level 1 and level 2 hierarchy
        for (PmsProductCategory category : categoryList) {
            Long parentId = category.getParentId();
            CategoryNode currentNode = nodeMap.get(category.getId());

            if (parentId == null || parentId == 0) {
                rootList.add(currentNode);
            } else {
                CategoryNode parentNode = nodeMap.get(parentId);
                if (parentNode != null) {
                    parentNode.getChildren().add(currentNode);
                }
            }
        }

        // step 3: attach products as level 3 children under second-level categories
        for (PmsProduct product : productList) {
            Long categoryId = product.getCategoryId();
            if (categoryId != null) {
                CategoryNode parentNode = nodeMap.get(categoryId);
                if (parentNode != null) {
                    CategoryNode productNode = new CategoryNode();
                    productNode.setId(product.getId());
                    productNode.setName(product.getName());
                    productNode.setParentId(categoryId);
                    productNode.setPrice(product.getPrice());
                    productNode.setChildren(new ArrayList<>());
                    parentNode.getChildren().add(productNode);
                }
            }
        }

        return rootList;
    }
}
