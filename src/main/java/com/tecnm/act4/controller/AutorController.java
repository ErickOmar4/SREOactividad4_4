package com.tecnm.act4.controller;

import com.tecnm.act4.dto.*;
import com.tecnm.act4.service.AutorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/autores")
public class AutorController {

    private final AutorService service;

    public AutorController(AutorService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<AutorResponse> crear(@Valid @RequestBody AutorRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(req));
    }

    @GetMapping
    public ResponseEntity<PageResponse<AutorResponse>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.listar(
                PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100), Sort.by("id"))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(id));
    }
}
