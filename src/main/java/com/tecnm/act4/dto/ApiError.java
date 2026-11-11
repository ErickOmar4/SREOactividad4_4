package com.tecnm.act4.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(String timestamp, int status, String error, String mensaje,
                       String ruta, Map<String, String> errores) { }
