package com.tecnm.act4.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnm.act4.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

/** Respuestas JSON para 401 (sin token / token invalido) y 403 (sin permisos). */
@Component
public class JsonSecurityHandlers implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper mapper;

    public JsonSecurityHandlers(ObjectMapper mapper) { this.mapper = mapper; }

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException ex)
            throws IOException {
        escribir(req, res, HttpStatus.UNAUTHORIZED, "Token JWT ausente, invalido o expirado");
    }

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException ex)
            throws IOException {
        escribir(req, res, HttpStatus.FORBIDDEN, "No tienes permisos para este recurso");
    }

    private void escribir(HttpServletRequest req, HttpServletResponse res, HttpStatus s, String msg)
            throws IOException {
        res.setStatus(s.value());
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setCharacterEncoding("UTF-8");
        mapper.writeValue(res.getWriter(), new ApiError(Instant.now().toString(), s.value(),
                s.getReasonPhrase(), msg, req.getRequestURI(), null));
    }
}
