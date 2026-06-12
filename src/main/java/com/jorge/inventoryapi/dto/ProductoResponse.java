package com.jorge.inventoryapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Respuesta con la informacion publica de un producto")
public class ProductoResponse {

    @Schema(description = "Identificador unico del producto generado por la base de datos", example = "1")
    private Long id;

    @Schema(description = "Nombre del producto", example = "Monitor Samsung")
    private String nombre;

    @Schema(description = "Descripcion del producto", example = "Monitor 24 pulgadas Full HD")
    private String descripcion;

    @Schema(description = "Cantidad disponible en inventario", example = "15")
    private Integer stock;

    @Schema(description = "Precio del producto", example = "650000.00")
    private BigDecimal precio;

    @Schema(description = "Categoria del producto", example = "Tecnologia")
    private String categoria;

    public ProductoResponse(Long id, String nombre, String descripcion, Integer stock, BigDecimal precio, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stock = stock;
        this.precio = precio;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Integer getStock() {
        return stock;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public String getCategoria() {
        return categoria;
    }
}

