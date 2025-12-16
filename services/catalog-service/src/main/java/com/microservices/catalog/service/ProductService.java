package com.microservices.catalog.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservices.catalog.dao.ProductDAO;
import com.microservices.catalog.dto.ProductDTO;
import com.microservices.catalog.mapper.ProductMapper;
import com.microservices.catalog.model.ProductEntity;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private ProductMapper productMapper;

    // Obtener todos los productos activos como DTOs
    public List<ProductDTO> getAllActiveProducts() {
        List<ProductEntity> entities = productDAO.findAllActive();
        return entities.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Obtener un producto por id
    public Optional<ProductDTO> getProductById(Long id) {
        return productDAO.findById(id).map(productMapper::toDTO);
    }

    // Productos por categoria
    public List<ProductDTO> getProductsByCategory(String category) {
        List<ProductEntity> entities = productDAO.findByCategory(category);
        return entities.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar por nombre
    public List<ProductDTO> searchProductsByName(String name) {
        List<ProductEntity> entities = productDAO.searchByName(name);
        return entities.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Listado de categorias
    public List<String> getAllCategories() {
        return productDAO.findDistinctCategories();
    }

    // Guardar producto (recibe DTO y devuelve DTO)
    public ProductDTO saveProduct(ProductDTO productDto) {
        ProductEntity entity = productMapper.fromDTO(productDto);
        ProductEntity saved = productDAO.save(entity);
        return productMapper.toDTO(saved);
    }

    // Actualizar producto
    public ProductDTO updateProduct(Long id, ProductDTO productDto) {
        Optional<ProductEntity> optionalProduct = productDAO.findById(id);
        if (optionalProduct.isPresent()) {
            ProductEntity product = optionalProduct.get();
            ProductEntity details = productMapper.fromDTO(productDto);
            // Copiar campos relevantes

            ProductEntity updated = productDAO.save(product);
            return productMapper.toDTO(updated);
        }
        return null;
    }

    // Borrado lógico
    public void deleteProduct(Long id) {
        Optional<ProductEntity> optionalProduct = productDAO.findById(id);
        if (optionalProduct.isPresent()) {
            ProductEntity product = optionalProduct.get();
            product.setActive(false);
            productDAO.save(product);
        }
    }

    // Actualizar stock
    public boolean updateStock(Long productId, Integer quantity) {
        Optional<ProductEntity> optionalProduct = productDAO.findById(productId);
        if (optionalProduct.isPresent()) {
            ProductEntity product = optionalProduct.get();
            if (product.getStock() >= quantity) {
                product.setStock(product.getStock() - quantity);
                productDAO.save(product);
                return true;
            }
        }
        return false;
    }
}
