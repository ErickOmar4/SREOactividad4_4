package com.tecnm.act4.dto;

/** Respuesta publica del usuario: NO incluye el password. */
public record UsuarioResponse(Long id, String nombre, String email, String rol) { }
