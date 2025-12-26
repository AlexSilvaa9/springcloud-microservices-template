package com.microservices.cart.mapper;

import org.mapstruct.Mapper;


import com.microservices.cart.dto.ShoppingCartDTO;
import com.microservices.cart.model.ShoppingCartEntity;

/**
 * Mapper MapStruct para convertir entre entidades del dominio y DTOs.
 */
@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {

    /**
     * Convierte una entidad de carrito a su DTO. MapStruct mapeará la lista de items.
     *
     * @param entity entidad de carrito
     * @return DTO del carrito
     */
    ShoppingCartDTO toDTO(ShoppingCartEntity entity);

    /**
     * Convierte un DTO de carrito a su entidad.
     *
     * @param dto DTO del carrito
     * @return entidad correspondiente
     */
    ShoppingCartEntity fromDTO(ShoppingCartDTO dto);



}
