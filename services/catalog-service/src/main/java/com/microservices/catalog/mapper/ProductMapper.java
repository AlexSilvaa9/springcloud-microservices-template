package com.microservices.catalog.mapper;

import org.mapstruct.Mapper;
import com.microservices.catalog.dto.ProductDTO;
import com.microservices.catalog.model.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDTO(ProductEntity producto);
    ProductEntity fromDTO(ProductDTO dto);
}
