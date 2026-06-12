package com.jorge.inventoryapi.service;

import com.jorge.inventoryapi.dto.ProductoRequest;
import com.jorge.inventoryapi.dto.ProductoResponse;
import com.jorge.inventoryapi.exception.ProductoNoEncontradoException;
import com.jorge.inventoryapi.model.Producto;
import com.jorge.inventoryapi.repository.ProductoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    @DisplayName("Debe crear un producto")
    void crearProducto() {
        ProductoRequest request = crearProductoRequest("Mouse Logitech", 10, "Tecnologia");
        Producto productoGuardado = new Producto(1L, "Mouse Logitech", "Mouse inalambrico", 10, BigDecimal.valueOf(120000), "Tecnologia");
        when(productoRepository.save(any(Producto.class))).thenReturn(productoGuardado);

        ProductoResponse response = productoService.guardar(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getNombre()).isEqualTo("Mouse Logitech");
        assertThat(response.getStock()).isEqualTo(10);
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    @DisplayName("Debe buscar productos por categoria")
    void buscarPorCategoria() {
        when(productoRepository.findByCategoriaIgnoreCase("Tecnologia"))
                .thenReturn(List.of(new Producto(1L, "Mouse Logitech", "Mouse inalambrico", 10, BigDecimal.valueOf(120000), "Tecnologia")));

        List<ProductoResponse> productos = productoService.buscarPorCategoria("Tecnologia");

        assertThat(productos).hasSize(1);
        assertThat(productos.get(0).getCategoria()).isEqualTo("Tecnologia");
        verify(productoRepository).findByCategoriaIgnoreCase("Tecnologia");
    }

    @Test
    @DisplayName("Debe buscar productos por nombre")
    void buscarPorNombre() {
        when(productoRepository.findByNombreContainingIgnoreCase("mouse"))
                .thenReturn(List.of(new Producto(1L, "Mouse Logitech", "Mouse inalambrico", 10, BigDecimal.valueOf(120000), "Tecnologia")));

        List<ProductoResponse> productos = productoService.buscarPorNombre("mouse");

        assertThat(productos).hasSize(1);
        assertThat(productos.get(0).getNombre()).isEqualTo("Mouse Logitech");
        verify(productoRepository).findByNombreContainingIgnoreCase("mouse");
    }

    @Test
    @DisplayName("Debe lanzar excepcion cuando el producto no existe")
    void productoInexistente() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productoService.buscarPorId(99L))
                .isInstanceOf(ProductoNoEncontradoException.class)
                .hasMessageContaining("99");

        verify(productoRepository).findById(99L);
    }

    private ProductoRequest crearProductoRequest(String nombre, Integer stock, String categoria) {
        ProductoRequest request = new ProductoRequest();
        request.setNombre(nombre);
        request.setDescripcion("Mouse inalambrico");
        request.setStock(stock);
        request.setPrecio(BigDecimal.valueOf(120000));
        request.setCategoria(categoria);
        return request;
    }
}
