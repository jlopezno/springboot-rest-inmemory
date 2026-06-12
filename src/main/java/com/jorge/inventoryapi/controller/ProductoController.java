package com.jorge.inventoryapi.controller;

import com.jorge.inventoryapi.dto.ProductoPageResponse;
import com.jorge.inventoryapi.dto.ProductoRequest;
import com.jorge.inventoryapi.dto.ProductoResponse;
import com.jorge.inventoryapi.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
@Tag(name = "Productos", description = "Operaciones CRUD para la gestion de productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    @Operation(summary = "Listar productos", description = "Obtiene todos los productos registrados")
    @ApiResponse(responseCode = "200", description = "Productos encontrados")
    public List<ProductoResponse> listar() {
        return productoService.listar();
    }

    @GetMapping("/page")
    @Operation(summary = "Listar productos paginados", description = "Obtiene productos usando paginacion. La primera pagina es 0.")
    @ApiResponse(responseCode = "200", description = "Pagina de productos obtenida correctamente")
    public ProductoPageResponse listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return productoService.listarPaginado(page, size);
    }

    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar productos por categoria", description = "Obtiene productos filtrados por categoria sin distinguir mayusculas o minusculas")
    @ApiResponse(responseCode = "200", description = "Productos encontrados")
    public List<ProductoResponse> buscarPorCategoria(@PathVariable String categoria) {
        return productoService.buscarPorCategoria(categoria);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar productos por nombre", description = "Obtiene productos cuyo nombre contiene el texto enviado")
    @ApiResponse(responseCode = "200", description = "Productos encontrados")
    public List<ProductoResponse> buscarPorNombre(@RequestParam String nombre) {
        return productoService.buscarPorNombre(nombre);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar producto por id", description = "Obtiene un producto usando su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<ProductoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear producto", description = "Registra un nuevo producto en el inventario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos")
    })
    public ResponseEntity<ProductoResponse> guardar(@Valid @RequestBody ProductoRequest producto) {
        ProductoResponse productoGuardado = productoService.guardar(producto);
        return ResponseEntity.status(201).body(productoGuardado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Actualiza los datos de un producto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<ProductoResponse> actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest producto) {
        return ResponseEntity.ok(productoService.actualizar(id, producto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto existente por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

