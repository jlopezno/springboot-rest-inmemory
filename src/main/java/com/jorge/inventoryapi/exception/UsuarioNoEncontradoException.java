package com.jorge.inventoryapi.exception;

public class UsuarioNoEncontradoException extends RuntimeException {

    public UsuarioNoEncontradoException(Long id) {
        super("No existe un usuario con id: " + id);
    }

    public UsuarioNoEncontradoException(String email) {
        super("No existe un usuario con email: " + email);
    }
}
