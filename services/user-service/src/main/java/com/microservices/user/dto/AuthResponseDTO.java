package com.microservices.user.dto;

import lombok.Data;

/**
 * DTO para respuesta de autenticación (JWT + usuario básico).
 */
@Data
public class AuthResponseDTO {
    private String token;
    private UserDTO user;

}
