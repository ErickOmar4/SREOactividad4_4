package com.tecnm.act4.service;

import com.tecnm.act4.dto.*;
import com.tecnm.act4.entity.Autor;
import com.tecnm.act4.exception.ResourceNotFoundException;
import com.tecnm.act4.repository.AutorRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutorService {

    private final AutorRepository repo;

    public AutorService(AutorRepository repo) { this.repo = repo; }

    @Transactional
    public AutorResponse crear(AutorRequest req) {
        Autor a = new Autor();
        a.setNombre(req.nombre().trim());
        a.setNacionalidad(req.nacionalidad());
        return aDto(repo.save(a));
    }

    @Transactional(readOnly = true)
    public PageResponse<AutorResponse> listar(Pageable pageable) {
        return PageResponse.de(repo.findAll(pageable).map(this::aDto));
    }

    @Transactional(readOnly = true)
    public AutorResponse obtener(Long id) {
        return aDto(repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor con id " + id + " no encontrado")));
    }

    private AutorResponse aDto(Autor a) {
        return new AutorResponse(a.getId(), a.getNombre(), a.getNacionalidad());
    }
}
