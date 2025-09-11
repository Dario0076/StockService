package com.inventario.StockService.controller;

import com.inventario.StockService.dto.ProductoDTO;
import com.inventario.StockService.dto.StockWithProductDTO;
import com.inventario.StockService.entity.Stock;
import com.inventario.StockService.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/stock")
@CrossOrigin("*")
public class StockController {
    private final StockService stockService;
    private final RestTemplate restTemplate;

    public StockController(StockService stockService, RestTemplate restTemplate) {
        this.stockService = stockService;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public List<StockWithProductDTO> getAllStock() {
        List<Stock> stocks = stockService.getAllStock();
        return stocks.stream().map(stock -> {
            String nombreProducto;
            
            // Obtener nombre del producto
            try {
                ProductoDTO producto = restTemplate.getForObject(
                    "http://localhost:8084/productos/" + stock.getProductoId(), 
                    ProductoDTO.class
                );
                nombreProducto = (producto != null) ? producto.getNombre() : "Producto no encontrado";
            } catch (Exception e) {
                nombreProducto = "Error al obtener nombre";
            }
            
            return new StockWithProductDTO(
                stock.getId(),
                stock.getProductoId(),
                stock.getCantidadActual(),
                stock.getUmbralMinimo(),
                nombreProducto
            );
        }).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> getStockById(@PathVariable Long id) {
        Optional<Stock> stock = stockService.getStockById(id);
        return stock.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<Stock> getStockByProductoId(@PathVariable Long productoId) {
        Optional<Stock> stock = stockService.getStockByProductoId(productoId);
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

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "StockService");
        status.put("timestamp", System.currentTimeMillis());
        return status;
    }
}

