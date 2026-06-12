package com.jorge.inventoryapi.controller;

import com.jorge.inventoryapi.dto.MovimientoInventarioRequest;
import com.jorge.inventoryapi.dto.MovimientoInventarioResponse;
import com.jorge.inventoryapi.service.MovimientoInventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/movimientos")
@Tag(name = "Movimientos de inventario", description = "Registro e historial de entradas y salidas de productos")
public class MovimientoInventarioController {

    private final MovimientoInventarioService movimientoInventarioService;

    public MovimientoInventarioController(MovimientoInventarioService movimientoInventarioService) {
        this.movimientoInventarioService = movimientoInventarioService;
    }

    @GetMapping
    @Operation(summary = "Listar movimientos", description = "Obtiene el historial de entradas y salidas de inventario")
    @ApiResponse(responseCode = "200", description = "Movimientos encontrados")
    public List<MovimientoInventarioResponse> listar() {
        return movimientoInventarioService.listar();
    }

    @PostMapping
    @Operation(summary = "Registrar movimiento", description = "Registra una entrada o salida y actualiza automaticamente el stock del producto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Movimiento registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "409", description = "Stock insuficiente para realizar la salida")
    })
    public ResponseEntity<MovimientoInventarioResponse> registrar(@Valid @RequestBody MovimientoInventarioRequest movimientoRequest) {
        MovimientoInventarioResponse movimientoGuardado = movimientoInventarioService.registrar(movimientoRequest);
        return ResponseEntity.status(201).body(movimientoGuardado);
    }
}
