package com.tecnm.act4.dto;

import org.springframework.data.domain.Page;
import java.util.List;

public record PageResponse<T>(List<T> contenido, int pagina, int tamanio,
                              long totalElementos, int totalPaginas, boolean ultima) {
    public static <T> PageResponse<T> de(Page<T> p) {
        return new PageResponse<>(p.getContent(), p.getNumber(), p.getSize(),
                p.getTotalElements(), p.getTotalPages(), p.isLast());
    }
}
