package com.inventario.StockService.controller;

import com.inventario.StockService.entity.Stock;
import com.inventario.StockService.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stock")
public class StockController {
    @Autowired
    private StockService stockService;

    @GetMapping
    public List<Stock> getAllStock() {
        return stockService.getAllStock();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> getStockById(@PathVariable Long id) {
        Optional<Stock> stock = stockService.getStockById(id);
        return stock.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Stock createStock(@RequestBody Stock stock) {
        return stockService.saveStock(stock);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable Long id, @RequestBody Stock stock) {
        if (!stockService.getStockById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Stock updated = stockService.updateStock(id, stock);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        if (!stockService.getStockById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        stockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para consultar productos con inventario bajo
    @GetMapping("/alerta-bajo")
    public List<Stock> getStockBajoPersonalizado() {
        return stockService.getStockBajoPersonalizado();
    }

    // Endpoint para actualizar el umbral mínimo de un producto
    @PutMapping("/{id}/umbral")
    public ResponseEntity<Stock> actualizarUmbral(@PathVariable Long id, @RequestParam Integer umbral) {
        Stock actualizado = stockService.actualizarUmbral(id, umbral);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }
}

