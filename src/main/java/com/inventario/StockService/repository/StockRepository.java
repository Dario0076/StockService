package com.inventario.StockService.repository;

import com.inventario.StockService.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findByCantidadActualLessThanEqual(Integer umbral);
    List<Stock> findByProductoId(Long productoId);
}

