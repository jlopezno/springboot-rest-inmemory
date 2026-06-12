package com.jorge.inventoryapi.exception;

public class StockInsuficienteException extends RuntimeException {

    public StockInsuficienteException(Long productoId, Integer stockActual, Integer cantidadSolicitada) {
        super("Stock insuficiente para el producto con id: " + productoId
                + ". Stock actual: " + stockActual
                + ", cantidad solicitada: " + cantidadSolicitada);
    }
}
