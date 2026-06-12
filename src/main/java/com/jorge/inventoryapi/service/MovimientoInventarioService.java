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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final ProductoRepository productoRepository;

    public MovimientoInventarioService(MovimientoInventarioRepository movimientoInventarioRepository, ProductoRepository productoRepository) {
        this.movimientoInventarioRepository = movimientoInventarioRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<MovimientoInventarioResponse> listar() {
        return movimientoInventarioRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MovimientoInventarioResponse registrar(MovimientoInventarioRequest movimientoRequest) {
        Producto producto = productoRepository.findById(movimientoRequest.getProductoId())
                .orElseThrow(() -> new ProductoNoEncontradoException(movimientoRequest.getProductoId()));

        aplicarMovimientoAlStock(producto, movimientoRequest);

        Producto productoActualizado = productoRepository.save(producto);

        MovimientoInventario movimiento = new MovimientoInventario(
                null,
                productoActualizado,
                movimientoRequest.getTipoMovimiento(),
                movimientoRequest.getCantidad(),
                LocalDateTime.now(),
                movimientoRequest.getObservacion()
        );

        MovimientoInventario movimientoGuardado = movimientoInventarioRepository.save(movimiento);
        return convertirAResponse(movimientoGuardado);
    }

    private void aplicarMovimientoAlStock(Producto producto, MovimientoInventarioRequest movimientoRequest) {
        Integer stockActual = producto.getStock();
        Integer cantidad = movimientoRequest.getCantidad();

        if (movimientoRequest.getTipoMovimiento() == TipoMovimiento.ENTRADA) {
            producto.setStock(stockActual + cantidad);
            return;
        }

        if (stockActual < cantidad) {
            throw new StockInsuficienteException(producto.getId(), stockActual, cantidad);
        }

        producto.setStock(stockActual - cantidad);
    }

    private MovimientoInventarioResponse convertirAResponse(MovimientoInventario movimiento) {
        Producto producto = movimiento.getProducto();

        return new MovimientoInventarioResponse(
                movimiento.getId(),
                producto.getId(),
                producto.getNombre(),
                movimiento.getTipoMovimiento(),
                movimiento.getCantidad(),
                movimiento.getFecha(),
                movimiento.getObservacion(),
                producto.getStock()
        );
    }
}
