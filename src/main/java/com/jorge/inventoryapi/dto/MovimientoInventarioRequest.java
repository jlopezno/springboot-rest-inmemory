package com.jorge.inventoryapi.dto;

import com.jorge.inventoryapi.model.TipoMovimiento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Datos necesarios para registrar un movimiento de inventario")
public class MovimientoInventarioRequest {

    @Schema(description = "Identificador del producto al que pertenece el movimiento. Campo obligatorio.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El productoId es obligatorio")
    private Long productoId;

    @Schema(description = "Tipo de movimiento. ENTRADA aumenta stock y SALIDA disminuye stock.", example = "ENTRADA", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El tipoMovimiento es obligatorio")
    private TipoMovimiento tipoMovimiento;

    @Schema(description = "Cantidad de unidades a mover. Debe ser mayor que cero.", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor que cero")
    private Integer cantidad;

    @Schema(description = "Observacion opcional del movimiento", example = "Compra inicial de inventario")
    private String observacion;

    public MovimientoInventarioRequest() {
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
