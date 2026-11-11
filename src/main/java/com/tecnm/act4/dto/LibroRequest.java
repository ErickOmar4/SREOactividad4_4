package com.tecnm.act4.dto;

import jakarta.validation.constraints.*;

public record LibroRequest(
        @NotBlank(message = "El titulo es obligatorio")
        @Size(max = 150, message = "El titulo no puede exceder 150 caracteres")
        String titulo,

        @NotBlank(message = "El ISBN es obligatorio")
        @Size(min = 10, max = 17, message = "El ISBN debe tener entre 10 y 17 caracteres")
        String isbn,

        @NotNull(message = "El anio de publicacion es obligatorio")
        @Min(value = 1000, message = "El anio debe ser mayor o igual a 1000")
        @Max(value = 2100, message = "El anio debe ser menor o igual a 2100")
        Integer anioPublicacion,

        @NotNull(message = "El autorId es obligatorio")
        Long autorId) { }
