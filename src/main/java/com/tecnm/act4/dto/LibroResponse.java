package com.tecnm.act4.dto;

/** Incluye el nombre del autor (no solo su id) para reflejar la relacion. */
public record LibroResponse(Long id, String titulo, String isbn, Integer anioPublicacion,
                            Long autorId, String autorNombre) { }
