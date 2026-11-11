package com.tecnm.act4.dto;

public record AuthResponse(String token, String tipo, long expiraEnMs, UsuarioResponse usuario) { }
