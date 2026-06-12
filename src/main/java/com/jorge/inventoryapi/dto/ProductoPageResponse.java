package com.jorge.inventoryapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Respuesta paginada de productos")
public class ProductoPageResponse {

    @Schema(description = "Productos incluidos en la pagina actual")
    private List<ProductoResponse> productos;

    @Schema(description = "Numero de pagina solicitada. La primera pagina es 0.", example = "0")
    private int page;

    @Schema(description = "Cantidad maxima de elementos por pagina", example = "5")
    private int size;

    @Schema(description = "Cantidad total de productos existentes", example = "25")
    private long totalElements;

    @Schema(description = "Cantidad total de paginas disponibles", example = "5")
    private int totalPages;

    @Schema(description = "Indica si la pagina actual es la ultima", example = "false")
    private boolean last;

    public ProductoPageResponse(List<ProductoResponse> productos, int page, int size, long totalElements, int totalPages, boolean last) {
        this.productos = productos;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

    public List<ProductoResponse> getProductos() {
        return productos;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public boolean isLast() {
        return last;
    }
}
