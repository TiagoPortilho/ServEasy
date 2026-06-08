package com.tiagoportilho.ServEasy.service;

import com.tiagoportilho.ServEasy.exception.ResourceNotFoundException;
import com.tiagoportilho.ServEasy.model.StockItem;
import com.tiagoportilho.ServEasy.repository.StockItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockItemRepository stockItemRepository;

    @Transactional(readOnly = true)
    public List<StockItem> getAllStockItems() {
        return stockItemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<StockItem> getStockItemById(Long id) {
        return stockItemRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<StockItem> getLowStockItems() {
        return stockItemRepository.findLowStockItems();
    }

    @Transactional
    public StockItem saveStockItem(StockItem stockItem) {
        return stockItemRepository.save(stockItem);
    }

    @Transactional
    public void deleteStockItem(Long id) {
        stockItemRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<StockItem> searchStockItems(String name) {
        return stockItemRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional
    public StockItem updateQuantity(Long id, Integer newQuantity) {
        StockItem stockItem = stockItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item de estoque", id));
        stockItem.setQuantity(newQuantity);
        return stockItemRepository.save(stockItem);
    }
}
