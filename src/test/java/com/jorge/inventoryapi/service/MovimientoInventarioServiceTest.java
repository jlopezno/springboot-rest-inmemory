package com.jorge.inventoryapi.service;

import com.jorge.inventoryapi.dto.MovimientoInventarioRequest;
import com.jorge.inventoryapi.dto.MovimientoInventarioResponse;
import com.jorge.inventoryapi.exception.ProductoNoEncontradoException;
import com.jorge.inventoryapi.exception.StockInsuficienteException;
import com.jorge.inventoryapi.model.MovimientoInventario;
import com.jorge.inventoryapi.model.Producto;
import com.jorge.inventoryapi.model.TipoMovimiento;
import com.jorge.inventoryapi.repository.MovimientoInventarioRepository;
import com.jorge.inventoryapi.repository.ProductoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovimientoInventarioServiceTest {

    @Mock
    private MovimientoInventarioRepository movimientoInventarioRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private MovimientoInventarioService movimientoInventarioService;

    @Test
    @DisplayName("ENTRADA debe aumentar el stock del producto")
    void entradaAumentaStock() {
        Producto producto = crearProducto(1L, 10);
        MovimientoInventarioRequest request = crearMovimientoRequest(1L, TipoMovimiento.ENTRADA, 5);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(producto)).thenReturn(producto);
        when(movimientoInventarioRepository.save(any(MovimientoInventario.class)))
                .thenAnswer(invocation -> new MovimientoInventario(1L, producto, TipoMovimiento.ENTRADA, 5, LocalDateTime.now(), "Compra"));

        MovimientoInventarioResponse response = movimientoInventarioService.registrar(request);

        assertThat(producto.getStock()).isEqualTo(15);
        assertThat(response.getStockActual()).isEqualTo(15);
        assertThat(response.getTipoMovimiento()).isEqualTo(TipoMovimiento.ENTRADA);
        verify(productoRepository).findById(1L);
        verify(productoRepository).save(producto);
        verify(movimientoInventarioRepository).save(any(MovimientoInventario.class));
    }

    @Test
    @DisplayName("SALIDA debe reducir el stock del producto")
    void salidaReduceStock() {
        Producto producto = crearProducto(1L, 10);
        MovimientoInventarioRequest request = crearMovimientoRequest(1L, TipoMovimiento.SALIDA, 4);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(producto)).thenReturn(producto);
        when(movimientoInventarioRepository.save(any(MovimientoInventario.class)))
                .thenAnswer(invocation -> new MovimientoInventario(1L, producto, TipoMovimiento.SALIDA, 4, LocalDateTime.now(), "Venta"));

        MovimientoInventarioResponse response = movimientoInventarioService.registrar(request);

        assertThat(producto.getStock()).isEqualTo(6);
        assertThat(response.getStockActual()).isEqualTo(6);
        assertThat(response.getTipoMovimiento()).isEqualTo(TipoMovimiento.SALIDA);
        verify(productoRepository).save(producto);
        verify(movimientoInventarioRepository).save(any(MovimientoInventario.class));
    }

    @Test
    @DisplayName("Debe lanzar excepcion cuando el producto no existe")
    void productoInexistente() {
        MovimientoInventarioRequest request = crearMovimientoRequest(99L, TipoMovimiento.ENTRADA, 5);
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movimientoInventarioService.registrar(request))
                .isInstanceOf(ProductoNoEncontradoException.class)
                .hasMessageContaining("99");

        verify(productoRepository).findById(99L);
        verify(productoRepository, never()).save(any(Producto.class));
        verify(movimientoInventarioRepository, never()).save(any(MovimientoInventario.class));
    }

    @Test
    @DisplayName("Debe lanzar excepcion cuando el stock es insuficiente")
    void stockInsuficiente() {
        Producto producto = crearProducto(1L, 3);
        MovimientoInventarioRequest request = crearMovimientoRequest(1L, TipoMovimiento.SALIDA, 5);
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        assertThatThrownBy(() -> movimientoInventarioService.registrar(request))
                .isInstanceOf(StockInsuficienteException.class)
                .hasMessageContaining("Stock insuficiente");

        assertThat(producto.getStock()).isEqualTo(3);
        verify(productoRepository).findById(1L);
        verify(productoRepository, never()).save(any(Producto.class));
        verify(movimientoInventarioRepository, never()).save(any(MovimientoInventario.class));
    }

    private Producto crearProducto(Long id, Integer stock) {
        return new Producto(id, "Mouse Logitech", "Mouse inalambrico", stock, BigDecimal.valueOf(120000), "Tecnologia");
    }

    private MovimientoInventarioRequest crearMovimientoRequest(Long productoId, TipoMovimiento tipoMovimiento, Integer cantidad) {
        MovimientoInventarioRequest request = new MovimientoInventarioRequest();
        request.setProductoId(productoId);
        request.setTipoMovimiento(tipoMovimiento);
        request.setCantidad(cantidad);
        request.setObservacion(tipoMovimiento == TipoMovimiento.ENTRADA ? "Compra" : "Venta");
        return request;
    }
}
