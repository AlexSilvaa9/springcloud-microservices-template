package com.microservices.catalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.microservices.catalog.model.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findByActiveTrue();

    List<ProductEntity> findByCategoryAndActiveTrue(String category);

    @Query("SELECT p FROM ProductEntity p WHERE p.name LIKE %:name% AND p.active = true")
    List<ProductEntity> findByNameContainingAndActiveTrue(@Param("name") String name);

    @Query("SELECT DISTINCT p.category FROM ProductEntity p WHERE p.active = true")
    List<String> findDistinctCategories();

}
