package com.jorge.inventoryapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta con la informacion publica de un usuario")
public class UsuarioResponse {

    @Schema(description = "Identificador unico del usuario generado por la base de datos", example = "1")
    private Long id;

    @Schema(description = "Nombre completo del usuario", example = "Carlos Perez")
    private String nombre;

    @Schema(description = "Correo electronico del usuario", example = "carlos@mail.com")
    private String email;

    public UsuarioResponse(Long id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }
}

