package com.microservices.catalog.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.microservices.catalog.model.ProductEntity;
import com.microservices.catalog.repository.ProductRepository;

@Component
public class ProductDAO {

    private final ProductRepository productRepository;

    public ProductDAO(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductEntity> findAllActive() {
        return productRepository.findByActiveTrue();
    }

    public Optional<ProductEntity> findById(Long id) {
        return productRepository.findById(id);
    }

    public List<ProductEntity> findByCategory(String category) {
        return productRepository.findByCategoryAndActiveTrue(category);
    }

    public List<ProductEntity> searchByName(String name) {
        return productRepository.findByNameContainingAndActiveTrue(name);
    }

    public List<String> findDistinctCategories() {
        return productRepository.findDistinctCategories();
    }

    public ProductEntity save(ProductEntity entity) {
        return productRepository.save(entity);
    }

}

