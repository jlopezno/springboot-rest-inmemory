package com.jorge.inventoryapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Datos necesarios para crear o actualizar un producto")
public class ProductoRequest {

    @Schema(
            description = "Nombre del producto. Campo obligatorio.",
            example = "Monitor Samsung",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Schema(
            description = "Descripcion opcional del producto",
            example = "Monitor 24 pulgadas Full HD"
    )
    private String descripcion;

    @Schema(
            description = "Cantidad disponible en inventario. Campo obligatorio y no puede ser negativo.",
            example = "15",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @Schema(
            description = "Precio del producto. Campo obligatorio y no puede ser negativo.",
            example = "650000.00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio no puede ser negativo")
    private BigDecimal precio;

    @Schema(
            description = "Categoria a la que pertenece el producto. Campo obligatorio.",
            example = "Tecnologia",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "La categoria es obligatoria")
    private String categoria;

    public ProductoRequest() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}

