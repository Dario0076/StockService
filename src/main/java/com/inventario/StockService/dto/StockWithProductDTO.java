package com.inventario.StockService.dto;

import lombok.Data;

@Data
public class StockWithProductDTO {
    private Long id;
    private Long productoId;
    private Integer cantidadActual;
    private Integer umbralMinimo;
    private String nombreProducto;
    
    public StockWithProductDTO(Long id, Long productoId, Integer cantidadActual, Integer umbralMinimo, String nombreProducto) {
        this.id = id;
        this.productoId = productoId;
        this.cantidadActual = cantidadActual;
        this.umbralMinimo = umbralMinimo;
        this.nombreProducto = nombreProducto;
    }
}
