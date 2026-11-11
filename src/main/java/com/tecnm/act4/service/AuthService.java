package com.tecnm.act4.service;

import com.tecnm.act4.dto.*;
import com.tecnm.act4.entity.Rol;
import com.tecnm.act4.entity.Usuario;
import com.tecnm.act4.exception.DuplicateResourceException;
import com.tecnm.act4.repository.UsuarioRepository;
import com.tecnm.act4.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwt;

    public AuthService(UsuarioRepository usuarios, PasswordEncoder encoder,
                       AuthenticationManager authManager, JwtService jwt) {
        this.usuarios = usuarios;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwt = jwt;
    }

    @Transactional
    public AuthResponse registrar(RegisterRequest req) {
        String email = req.email().trim().toLowerCase();
        if (usuarios.existsByEmail(email)) {
            throw new DuplicateResourceException("Ya existe un usuario con el email " + email);
        }
        Usuario u = new Usuario();
        u.setNombre(req.nombre().trim());
        u.setEmail(email);
        u.setPassword(encoder.encode(req.password()));
        u.setRol(Rol.USER);
        u = usuarios.save(u);
        return respuesta(u);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        String email = req.email().trim().toLowerCase();
        // Lanza BadCredentialsException si el email o la contrasena no coinciden
        authManager.authenticate(new UsernamePasswordAuthenticationToken(email, req.password()));
        Usuario u = usuarios.findByEmail(email).orElseThrow();
        return respuesta(u);
    }

    private AuthResponse respuesta(Usuario u) {
        String token = jwt.generarToken(u.getEmail(), u.getRol().name());
        return new AuthResponse(token, "Bearer", jwt.getExpirationMs(),
                new UsuarioResponse(u.getId(), u.getNombre(), u.getEmail(), u.getRol().name()));
    }
}
