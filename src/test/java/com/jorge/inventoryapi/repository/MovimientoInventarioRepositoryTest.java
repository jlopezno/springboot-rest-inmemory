package com.jorge.inventoryapi.repository;

import com.jorge.inventoryapi.model.MovimientoInventario;
import com.jorge.inventoryapi.model.Producto;
import com.jorge.inventoryapi.model.TipoMovimiento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = "spring.sql.init.mode=never")
class MovimientoInventarioRepositoryTest {

    @Autowired
    private MovimientoInventarioRepository movimientoInventarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @BeforeEach
    void limpiarBaseDeDatos() {
        movimientoInventarioRepository.deleteAll();
        productoRepository.deleteAll();
    }

    @Test
    @DisplayName("Debe guardar un movimiento de inventario asociado a un producto")
    void guardarMovimientoInventario() {
        Producto producto = productoRepository.save(crearProducto());
        MovimientoInventario movimiento = new MovimientoInventario(
                null,
                producto,
                TipoMovimiento.ENTRADA,
                5,
                LocalDateTime.now(),
                "Compra inicial"
        );

        MovimientoInventario movimientoGuardado = movimientoInventarioRepository.save(movimiento);

        Optional<MovimientoInventario> resultado = movimientoInventarioRepository.findById(movimientoGuardado.getId());
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getProducto().getId()).isEqualTo(producto.getId());
        assertThat(resultado.get().getTipoMovimiento()).isEqualTo(TipoMovimiento.ENTRADA);
        assertThat(resultado.get().getCantidad()).isEqualTo(5);
    }

    @Test
    @DisplayName("Debe ejecutar consultas basicas de JpaRepository")
    void consultasBasicasJpaRepository() {
        Producto producto = productoRepository.save(crearProducto());
        movimientoInventarioRepository.save(new MovimientoInventario(null, producto, TipoMovimiento.ENTRADA, 10, LocalDateTime.now(), "Entrada"));
        movimientoInventarioRepository.save(new MovimientoInventario(null, producto, TipoMovimiento.SALIDA, 3, LocalDateTime.now(), "Salida"));

        List<MovimientoInventario> movimientos = movimientoInventarioRepository.findAll();

        assertThat(movimientos).hasSize(2);
        assertThat(movimientoInventarioRepository.count()).isEqualTo(2);
        assertThat(movimientos)
                .extracting(MovimientoInventario::getTipoMovimiento)
                .containsExactlyInAnyOrder(TipoMovimiento.ENTRADA, TipoMovimiento.SALIDA);
    }

    private Producto crearProducto() {
        return new Producto(null, "Mouse Logitech", "Mouse inalambrico", 10, BigDecimal.valueOf(120000), "Tecnologia");
    }
}
