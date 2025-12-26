package com.microservices.cart.service;

import java.util.ArrayList;
import java.util.Optional;


import com.microservices.cart.mapper.CartItemMapper;
import com.microservices.cart.mapper.ShoppingCartMapper;
import com.microservices.core.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservices.cart.client.UserServiceClient;
import com.microservices.cart.dao.ShoppingCartDAO;
import com.microservices.cart.dto.CartItemDTO;
import com.microservices.cart.dto.ShoppingCartDTO;
import com.microservices.cart.model.CartItemEntity;
import com.microservices.cart.model.ShoppingCartEntity;

/**
 * Servicio público que expone operaciones sobre carritos para clientes.
 *
 * Si existe un User Service disponible, se valida que el usuario esté activo
 * antes de crear o modificar carritos. El cliente de UserService se inyecta
 * opcionalmente, de forma que el servicio de carrito puede funcionar sin
 * depender del User Service durante pruebas locales o despliegues parciales.
 */
@Service
@Transactional
public class ShoppingCartService {
    @Value("${app.environment}")
    private String environment;

    private final ShoppingCartDAO dao;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartMapper shoppingCartMapper;
    private final Optional<UserServiceClient> userClient;

    public ShoppingCartService(ShoppingCartDAO dao, CartItemMapper cartItemMapper, ShoppingCartMapper shoppingCartMapper, Optional<UserServiceClient> userClient) {
        this.dao = dao;
        this.shoppingCartMapper = shoppingCartMapper;
        this.cartItemMapper = cartItemMapper;
        this.userClient = userClient;
    }

    /**
     * Comprueba si un usuario está activo. Usa reflexión para tolerar getters generados por Lombok
     * (puede ser isActive() para boolean primitivo o getActive() para Boolean wrapper) y también
     * intenta leer el campo "active" si no hay getter disponible.
     */
    private boolean isUserActive(String userId) {
        try {
            // Si no hay cliente de UserService, permitimos la operación (se asume
            // que la verificación se hará en otro punto en infra). Si prefieres
            // denegar en ausencia del servicio, cambia el orElse a false.
            return userClient.map(c -> {
                if ("local".equals(environment)) {
                    return true;
                } else {
                    try {
                        UserDTO u = c.getUserById(userId);
                        if (u == null) return false;

                        // Intentar isActive()
                        try {
                            java.lang.reflect.Method m = u.getClass().getMethod("isActive");
                            Object r = m.invoke(u);
                            return r instanceof Boolean && (Boolean) r;
                        } catch (NoSuchMethodException ignore) {
                            // Intentar getActive()
                            try {
                                java.lang.reflect.Method m2 = u.getClass().getMethod("getActive");
                                Object r2 = m2.invoke(u);
                                return r2 instanceof Boolean && (Boolean) r2;
                            } catch (NoSuchMethodException ignore2) {
                                // Intentar acceso directo al campo "active"
                                try {
                                    java.lang.reflect.Field f = u.getClass().getDeclaredField("active");
                                    f.setAccessible(true);
                                    Object v = f.get(u);
                                    return v instanceof Boolean && (Boolean) v;
                                } catch (Exception ex) {
                                    return false;
                                }
                            } catch (Exception ex) {
                                return false;
                            }
                        } catch (Exception ex) {
                            return false;
                        }

                    } catch (Exception e) {
                        return false;
                    }
                }
            }).orElse(true);
        } catch (Exception ex) {
            // En caso de error durante la llamada, se considera que el usuario no está activo
            return false;
        }
    }

    /**
     * Crea un nuevo carrito para el usuario si no existe.
     *
     * @param userId identificador del usuario
     * @return DTO del carrito creado o existente
     */
    public ShoppingCartDTO createCartIfAbsent(String userId) {
        if (!isUserActive(userId)) {
            throw new IllegalStateException("User not active or not found: " + userId);
        }

        Optional<ShoppingCartEntity> ex = dao.findByUserId(userId);
        if (ex.isPresent()) {
            return shoppingCartMapper.toDTO(ex.get());
        }
        ShoppingCartEntity cart = new ShoppingCartEntity();
        cart.setUserId(userId);
        cart.setItems(new ArrayList<>());
        ShoppingCartEntity saved = dao.save(cart);
        return shoppingCartMapper.toDTO(saved);
    }

    /**
     * Obtiene el carrito del usuario si existe.
     *
     * @param userId identificador del usuario
     * @return Optional con el DTO del carrito
     */
    public Optional<ShoppingCartDTO> getCartByUserId(String userId) {
        return dao.findByUserId(userId).map(shoppingCartMapper::toDTO);
    }

    /**
     * Añade un item al carrito del usuario (crea carrito si es necesario).
     *
     * @param userId identificador del usuario
     * @param itemDTO DTO del item a añadir
     * @return DTO del carrito actualizado
     */
    public ShoppingCartDTO addItemToCart(String userId, CartItemDTO itemDTO) {
        if (!isUserActive(userId)) {
            throw new IllegalStateException("User not active or not found: " + userId);
        }

        ShoppingCartEntity cart = dao.findByUserId(userId).orElseGet(() -> {
            ShoppingCartEntity c = new ShoppingCartEntity();
            c.setUserId(userId);
            c.setItems(new ArrayList<>());
            return c;
        });

        CartItemEntity item = cartItemMapper.fromDTO(itemDTO);
        // asegurar relación bidireccional
        item.setCart(cart);
        cart.getItems().add(item);
        ShoppingCartEntity saved = dao.save(cart);
        return shoppingCartMapper.toDTO(saved);
    }

    /**
     * Elimina un item del carrito del usuario.
     *
     * @param userId identificador del usuario
     * @param itemId identificador del item
     * @return DTO del carrito actualizado
     */
    public Optional<ShoppingCartDTO> removeItemFromCart(String userId, Long itemId) {
        if (!isUserActive(userId)) {
            return Optional.empty();
        }
        Optional<ShoppingCartEntity> maybe = dao.findByUserId(userId);
        if (maybe.isEmpty()) return Optional.empty();
        ShoppingCartEntity cart = maybe.get();
        cart.getItems().removeIf(i -> i.getId() != null && i.getId().equals(itemId));
        ShoppingCartEntity saved = dao.save(cart);
        return Optional.of(shoppingCartMapper.toDTO(saved));
    }

    /**
     * Limpia (vacía) el carrito del usuario.
     *
     * @param userId identificador del usuario
     */
    public void clearCart(String userId) {
        if (!isUserActive(userId)) return;
        dao.findByUserId(userId).ifPresent(cart -> {
            cart.getItems().clear();
            dao.save(cart);
        });
    }

}
