package com.microservices.catalog.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public class GenericResponse<T> {
    private boolean success;
    private String message;
    private List<String> errors;
    private T data;
}
