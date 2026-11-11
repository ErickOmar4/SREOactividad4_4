package com.tecnm.act4.controller;

import com.tecnm.act4.dto.*;
import com.tecnm.act4.service.LibroService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService service;

    public LibroController(LibroService service) { this.service = service; }

    /** GET /api/libros?page=0&size=10&sortBy=titulo&dir=asc */
    @GetMapping
    public ResponseEntity<PageResponse<LibroResponse>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String dir) {
        // Lista blanca de campos ordenables para evitar errores / abusos
        String campo = switch (sortBy) {
            case "titulo", "isbn", "anioPublicacion" -> sortBy;
            default -> "id";
        };
        Sort sort = "desc".equalsIgnoreCase(dir) ? Sort.by(campo).descending() : Sort.by(campo).ascending();
        return ResponseEntity.ok(service.listar(
                PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100), sort)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    @PostMapping
    public ResponseEntity<LibroResponse> crear(@Valid @RequestBody LibroRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibroResponse> actualizar(@PathVariable Long id,
                                                    @Valid @RequestBody LibroRequest req) {
        return ResponseEntity.ok(service.actualizar(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
