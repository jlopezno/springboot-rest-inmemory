package com.jorge.inventoryapi.dto;

import com.jorge.inventoryapi.model.TipoMovimiento;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Respuesta con la informacion de un movimiento de inventario")
public class MovimientoInventarioResponse {

    @Schema(description = "Identificador unico del movimiento", example = "1")
    private Long id;

    @Schema(description = "Identificador del producto relacionado", example = "1")
    private Long productoId;

    @Schema(description = "Nombre del producto relacionado", example = "Mouse Logitech")
    private String productoNombre;

    @Schema(description = "Tipo de movimiento realizado", example = "ENTRADA")
    private TipoMovimiento tipoMovimiento;

    @Schema(description = "Cantidad de unidades movidas", example = "5")
    private Integer cantidad;

    @Schema(description = "Fecha y hora en que se registro el movimiento", example = "2026-06-12T10:30:00")
    private LocalDateTime fecha;

    @Schema(description = "Observacion registrada para el movimiento", example = "Compra inicial de inventario")
    private String observacion;

    @Schema(description = "Stock del producto despues de aplicar el movimiento", example = "25")
    private Integer stockActual;

    public MovimientoInventarioResponse(Long id, Long productoId, String productoNombre, TipoMovimiento tipoMovimiento, Integer cantidad, LocalDateTime fecha, String observacion, Integer stockActual) {
        this.id = id;
        this.productoId = productoId;
        this.productoNombre = productoNombre;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.observacion = observacion;
        this.stockActual = stockActual;
    }

    public Long getId() {
        return id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getObservacion() {
        return observacion;
    }

    public Integer getStockActual() {
        return stockActual;
    }
}
