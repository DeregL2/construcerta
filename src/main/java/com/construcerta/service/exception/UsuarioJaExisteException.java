package com.construcerta.service.exception;

public class UsuarioJaExisteException extends RuntimeException {
    public UsuarioJaExisteException(String message) {
        super(message);
    }
}
