package com.tecnm.act4.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AutorRequest(
        @NotBlank(message = "El nombre del autor es obligatorio")
        @Size(max = 120, message = "El nombre no puede exceder 120 caracteres")
        String nombre,

        @Size(max = 80, message = "La nacionalidad no puede exceder 80 caracteres")
        String nacionalidad) { }
