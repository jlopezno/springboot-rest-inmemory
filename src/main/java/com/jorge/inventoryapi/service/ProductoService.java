package com.jorge.inventoryapi.service;

import com.jorge.inventoryapi.dto.ProductoPageResponse;
import com.jorge.inventoryapi.dto.ProductoRequest;
import com.jorge.inventoryapi.dto.ProductoResponse;
import com.jorge.inventoryapi.exception.ProductoNoEncontradoException;
import com.jorge.inventoryapi.model.Producto;
import com.jorge.inventoryapi.repository.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<ProductoResponse> listar() {
        return productoRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public ProductoPageResponse listarPaginado(int page, int size) {
        Page<Producto> productosPage = productoRepository.findAll(PageRequest.of(page, size));

        List<ProductoResponse> productos = productosPage.getContent().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());

        return new ProductoPageResponse(
                productos,
                productosPage.getNumber(),
                productosPage.getSize(),
                productosPage.getTotalElements(),
                productosPage.getTotalPages(),
                productosPage.isLast()
        );
    }

    public ProductoResponse buscarPorId(Long id) {
        Producto producto = obtenerProductoPorId(id);
        return convertirAResponse(producto);
    }

    public List<ProductoResponse> buscarPorCategoria(String categoria) {
        return productoRepository.findByCategoriaIgnoreCase(categoria).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public List<ProductoResponse> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public ProductoResponse guardar(ProductoRequest productoRequest) {
        Producto producto = convertirAModelo(productoRequest);
        Producto productoGuardado = productoRepository.save(producto);
        return convertirAResponse(productoGuardado);
    }

    public ProductoResponse actualizar(Long id, ProductoRequest productoActualizado) {
        Producto producto = obtenerProductoPorId(id);
        producto.setNombre(productoActualizado.getNombre());
        producto.setDescripcion(productoActualizado.getDescripcion());
        producto.setStock(productoActualizado.getStock());
        producto.setPrecio(productoActualizado.getPrecio());
        producto.setCategoria(productoActualizado.getCategoria());

        Producto productoGuardado = productoRepository.save(producto);
        return convertirAResponse(productoGuardado);
    }

    public void eliminar(Long id) {
        obtenerProductoPorId(id);
        productoRepository.deleteById(id);
    }

    private Producto convertirAModelo(ProductoRequest productoRequest) {
        return new Producto(
                null,
                productoRequest.getNombre(),
                productoRequest.getDescripcion(),
                productoRequest.getStock(),
                productoRequest.getPrecio(),
                productoRequest.getCategoria()
        );
    }

    private ProductoResponse convertirAResponse(Producto producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getStock(),
                producto.getPrecio(),
                producto.getCategoria()
        );
    }

    private Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(id));
    }
}

