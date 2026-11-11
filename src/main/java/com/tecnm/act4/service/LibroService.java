package com.tecnm.act4.service;

import com.tecnm.act4.dto.*;
import com.tecnm.act4.entity.Autor;
import com.tecnm.act4.entity.Libro;
import com.tecnm.act4.exception.DuplicateResourceException;
import com.tecnm.act4.exception.ResourceNotFoundException;
import com.tecnm.act4.repository.AutorRepository;
import com.tecnm.act4.repository.LibroRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Logica de negocio del CRUD de libros (el controlador solo traduce HTTP). */
@Service
public class LibroService {

    private final LibroRepository libros;
    private final AutorRepository autores;

    public LibroService(LibroRepository libros, AutorRepository autores) {
        this.libros = libros;
        this.autores = autores;
    }

    @Transactional
    public LibroResponse crear(LibroRequest req) {
        if (libros.existsByIsbn(req.isbn())) {
            throw new DuplicateResourceException("Ya existe un libro con el ISBN " + req.isbn());
        }
        Libro l = new Libro();
        aplicar(l, req);
        return aDto(libros.save(l));
    }

    @Transactional(readOnly = true)
    public PageResponse<LibroResponse> listar(Pageable pageable) {
        return PageResponse.de(libros.findAll(pageable).map(this::aDto));
    }

    @Transactional(readOnly = true)
    public LibroResponse obtener(Long id) {
        return aDto(buscar(id));
    }

    @Transactional
    public LibroResponse actualizar(Long id, LibroRequest req) {
        Libro l = buscar(id);
        if (libros.existsByIsbnAndIdNot(req.isbn(), id)) {
            throw new DuplicateResourceException("Ya existe otro libro con el ISBN " + req.isbn());
        }
        aplicar(l, req);
        return aDto(libros.save(l));
    }

    @Transactional
    public void eliminar(Long id) {
        libros.delete(buscar(id));
    }

    private Libro buscar(Long id) {
        return libros.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro con id " + id + " no encontrado"));
    }

    private void aplicar(Libro l, LibroRequest req) {
        Autor autor = autores.findById(req.autorId())
                .orElseThrow(() -> new ResourceNotFoundException("Autor con id " + req.autorId() + " no encontrado"));
        l.setTitulo(req.titulo().trim());
        l.setIsbn(req.isbn().trim());
        l.setAnioPublicacion(req.anioPublicacion());
        l.setAutor(autor);
    }

    private LibroResponse aDto(Libro l) {
        return new LibroResponse(l.getId(), l.getTitulo(), l.getIsbn(), l.getAnioPublicacion(),
                l.getAutor().getId(), l.getAutor().getNombre());
    }
}
