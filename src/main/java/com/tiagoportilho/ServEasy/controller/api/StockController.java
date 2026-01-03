package com.tiagoportilho.ServEasy.controller.api;

import com.tiagoportilho.ServEasy.dto.ApiResponse;
import com.tiagoportilho.ServEasy.model.StockItem;
import com.tiagoportilho.ServEasy.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StockItem>>> getAllStockItems() {
        try {
            List<StockItem> stockItems = stockService.getAllStockItems();
            return ResponseEntity.ok(ApiResponse.success(stockItems));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao buscar itens do estoque: " + e.getMessage()));
        }
    }

    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<StockItem>>> getLowStockItems() {
        try {
            List<StockItem> stockItems = stockService.getLowStockItems();
            return ResponseEntity.ok(ApiResponse.success(stockItems));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao buscar itens com estoque baixo: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StockItem>> getStockItemById(@PathVariable Long id) {
        try {
            Optional<StockItem> stockItem = stockService.getStockItemById(id);
            if (stockItem.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success(stockItem.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao buscar item: " + e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<StockItem>>> searchStockItems(@RequestParam String name) {
        try {
            List<StockItem> stockItems = stockService.searchStockItems(name);
            return ResponseEntity.ok(ApiResponse.success(stockItems));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao pesquisar itens: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StockItem>> createStockItem(@RequestBody StockItem stockItem) {
        try {
            StockItem savedItem = stockService.saveStockItem(stockItem);
            return ResponseEntity.ok(ApiResponse.success("Item criado com sucesso", savedItem));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao criar item: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StockItem>> updateStockItem(@PathVariable Long id, @RequestBody StockItem stockItem) {
        try {
            stockItem.setId(id);
            StockItem updatedItem = stockService.saveStockItem(stockItem);
            return ResponseEntity.ok(ApiResponse.success("Item atualizado com sucesso", updatedItem));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao atualizar item: " + e.getMessage()));
        }
    }

    @PatchMapping("/{id}/quantity")
    public ResponseEntity<ApiResponse<StockItem>> updateQuantity(@PathVariable Long id, @RequestParam Integer quantity) {
        try {
            StockItem updatedItem = stockService.updateQuantity(id, quantity);
            return ResponseEntity.ok(ApiResponse.success("Quantidade atualizada com sucesso", updatedItem));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao atualizar quantidade: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStockItem(@PathVariable Long id) {
        try {
            stockService.deleteStockItem(id);
            return ResponseEntity.ok(ApiResponse.success("Item deletado com sucesso", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao deletar item: " + e.getMessage()));
        }
    }
}