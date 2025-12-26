package com.microservices.user.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

/**
 * DTO que expone la información pública del usuario (sin password).
 */
@Data
public class UserDTO {
    private UUID id;
    private String email;
    private String role;
    private LocalDateTime createdAt;

}
