package com.inventario.StockService.service;

import com.inventario.StockService.entity.Stock;
import com.inventario.StockService.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;

    public List<Stock> getAllStock() {
        return stockRepository.findAll();
    }

    public Optional<Stock> getStockById(Long id) {
        return stockRepository.findById(id);
    }

    public Stock saveStock(Stock stock) {
        return stockRepository.save(stock);
    }

    public Stock updateStock(Long id, Stock stock) {
        stock.setId(id);
        return stockRepository.save(stock);
    }

    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }

    public List<Stock> getStockBajoPersonalizado() {
        return stockRepository.findAll().stream()
                .filter(s -> s.getCantidadActual() <= s.getUmbralMinimo())
                .toList();
    }

    public Stock actualizarUmbral(Long id, Integer nuevoUmbral) {
        Optional<Stock> stockOpt = stockRepository.findById(id);
        if (stockOpt.isPresent()) {
            Stock stock = stockOpt.get();
            stock.setUmbralMinimo(nuevoUmbral);
            return stockRepository.save(stock);
        }
        return null;
    }
}
