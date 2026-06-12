package com.jorge.inventoryapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "Formato estandar de respuesta para errores de la API")
public class ErrorResponse {

    @Schema(description = "Fecha y hora en la que ocurrio el error", example = "2026-06-12T10:30:00")
    private final LocalDateTime timestamp;

    @Schema(description = "Codigo HTTP asociado al error", example = "400")
    private final int status;

    @Schema(description = "Nombre del estado HTTP", example = "Bad Request")
    private final String error;

    @Schema(description = "Mensaje general del error", example = "La solicitud tiene campos invalidos")
    private final String message;

    @Schema(
            description = "Mapa de errores de validacion por campo. Puede ser null si el error no es de validacion.",
            example = "{\"nombre\":\"El nombre es obligatorio\",\"email\":\"El email debe tener un formato valido\"}"
    )
    private final Map<String, String> validationErrors;

    public ErrorResponse(int status, String error, String message, Map<String, String> validationErrors) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.validationErrors = validationErrors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }
}

