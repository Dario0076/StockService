package com.inventario.StockService.dto;

import lombok.Data;

@Data
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer cantidad;
    private Boolean activo;
}
