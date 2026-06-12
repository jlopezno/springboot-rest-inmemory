package com.jorge.inventoryapi.repository;

import com.jorge.inventoryapi.model.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = "spring.sql.init.mode=never")
class ProductoRepositoryTest {

    @Autowired
    private ProductoRepository productoRepository;

    @BeforeEach
    void limpiarBaseDeDatos() {
        productoRepository.deleteAll();
    }

    @Test
    @DisplayName("Debe guardar un producto y consultarlo por id")
    void guardarProducto() {
        Producto producto = crearProducto("Mouse Logitech", "Mouse inalambrico", 10, "Tecnologia");

        Producto productoGuardado = productoRepository.save(producto);

        Optional<Producto> resultado = productoRepository.findById(productoGuardado.getId());
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Mouse Logitech");
        assertThat(resultado.get().getStock()).isEqualTo(10);
    }

    @Test
    @DisplayName("Debe buscar productos por categoria ignorando mayusculas")
    void buscarPorCategoriaIgnoreCase() {
        productoRepository.save(crearProducto("Mouse Logitech", "Mouse inalambrico", 10, "Tecnologia"));
        productoRepository.save(crearProducto("Teclado Redragon", "Teclado mecanico", 5, "tecnologia"));
        productoRepository.save(crearProducto("Silla Oficina", "Silla ergonomica", 3, "Muebles"));

        List<Producto> productos = productoRepository.findByCategoriaIgnoreCase("TECNOLOGIA");

        assertThat(productos)
                .hasSize(2)
                .extracting(Producto::getNombre)
                .containsExactlyInAnyOrder("Mouse Logitech", "Teclado Redragon");
    }

    @Test
    @DisplayName("Debe buscar productos por nombre parcial ignorando mayusculas")
    void buscarPorNombreContainingIgnoreCase() {
        productoRepository.save(crearProducto("Monitor Samsung", "Monitor 24 pulgadas", 8, "Tecnologia"));
        productoRepository.save(crearProducto("Monitor LG", "Monitor 27 pulgadas", 6, "Tecnologia"));
        productoRepository.save(crearProducto("Mouse Logitech", "Mouse inalambrico", 10, "Tecnologia"));

        List<Producto> productos = productoRepository.findByNombreContainingIgnoreCase("monitor");

        assertThat(productos)
                .hasSize(2)
                .extracting(Producto::getNombre)
                .containsExactlyInAnyOrder("Monitor Samsung", "Monitor LG");
    }

    @Test
    @DisplayName("Debe ejecutar consultas basicas de JpaRepository")
    void consultasBasicasJpaRepository() {
        productoRepository.save(crearProducto("Mouse Logitech", "Mouse inalambrico", 10, "Tecnologia"));
        productoRepository.save(crearProducto("Silla Oficina", "Silla ergonomica", 3, "Muebles"));

        List<Producto> productos = productoRepository.findAll();

        assertThat(productos).hasSize(2);
        assertThat(productoRepository.count()).isEqualTo(2);
    }

    private Producto crearProducto(String nombre, String descripcion, Integer stock, String categoria) {
        return new Producto(null, nombre, descripcion, stock, BigDecimal.valueOf(120000), categoria);
    }
}
