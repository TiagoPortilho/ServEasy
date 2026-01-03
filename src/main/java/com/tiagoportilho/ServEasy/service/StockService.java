package com.tiagoportilho.ServEasy.service;

import com.tiagoportilho.ServEasy.model.StockItem;
import com.tiagoportilho.ServEasy.repository.StockItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class StockService {
    
    private final StockItemRepository stockItemRepository;

    public List<StockItem> getAllStockItems() {
        return stockItemRepository.findAll();
    }

    public Optional<StockItem> getStockItemById(Long id) {
        return stockItemRepository.findById(id);
    }

    public List<StockItem> getLowStockItems() {
        return stockItemRepository.findLowStockItems();
    }

    public StockItem saveStockItem(StockItem stockItem) {
        return stockItemRepository.save(stockItem);
    }

    public void deleteStockItem(Long id) {
        stockItemRepository.deleteById(id);
    }

    public List<StockItem> searchStockItems(String name) {
        return stockItemRepository.findByNameContainingIgnoreCase(name);
    }

    public StockItem updateQuantity(Long id, Integer newQuantity) {
        Optional<StockItem> stockItemOpt = stockItemRepository.findById(id);
        if (stockItemOpt.isPresent()) {
            StockItem stockItem = stockItemOpt.get();
            stockItem.setQuantity(newQuantity);
            return stockItemRepository.save(stockItem);
        }
        throw new RuntimeException("Item de estoque não encontrado");
    }
}