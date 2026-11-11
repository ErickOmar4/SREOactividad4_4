package com.tecnm.act4.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String mensaje) { super(mensaje); }
}
