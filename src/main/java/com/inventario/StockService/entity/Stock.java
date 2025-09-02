package com.inventario.StockService.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "stock")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;
    
    @Column(name = "cantidad_actual", nullable = false)
    private Integer cantidadActual;
    
    @Column(name = "umbral_minimo", nullable = false)
    private Integer umbralMinimo;
}

