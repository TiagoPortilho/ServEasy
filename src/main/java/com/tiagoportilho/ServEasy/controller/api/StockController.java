package com.tiagoportilho.ServEasy.controller.api;

import com.tiagoportilho.ServEasy.dto.ApiResponse;
import com.tiagoportilho.ServEasy.exception.ResourceNotFoundException;
import com.tiagoportilho.ServEasy.model.StockItem;
import com.tiagoportilho.ServEasy.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
@Tag(name = "Estoque", description = "Controle de ingredientes e itens do estoque")
public class StockController {

    private final StockService stockService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE_ATENDENTE')")
    @Operation(summary = "Listar estoque", description = "Retorna todos os itens do estoque. Acessível por ADMIN e CLIENTE_ATENDENTE.")
    public ResponseEntity<ApiResponse<List<StockItem>>> getAllStockItems() {
        return ResponseEntity.ok(ApiResponse.success(stockService.getAllStockItems()));
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Itens com estoque baixo", description = "Retorna itens abaixo do nível mínimo. Somente ADMIN.")
    public ResponseEntity<ApiResponse<List<StockItem>>> getLowStockItems() {
        return ResponseEntity.ok(ApiResponse.success(stockService.getLowStockItems()));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Pesquisar itens", description = "Busca itens do estoque por nome (case-insensitive). Somente ADMIN.")
    public ResponseEntity<ApiResponse<List<StockItem>>> searchStockItems(
            @Parameter(description = "Texto para busca no nome", example = "farinha") @RequestParam String name) {
        return ResponseEntity.ok(ApiResponse.success(stockService.searchStockItems(name)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar item por ID", description = "Retorna um item do estoque. Retorna 404 se não encontrado.")
    public ResponseEntity<ApiResponse<StockItem>> getStockItemById(
            @Parameter(description = "ID do item", example = "1") @PathVariable Long id) {
        StockItem item = stockService.getStockItemById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item de estoque", id));
        return ResponseEntity.ok(ApiResponse.success(item));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar item no estoque", description = "Cria um novo item de estoque. Somente ADMIN.")
    public ResponseEntity<ApiResponse<StockItem>> createStockItem(@RequestBody StockItem stockItem) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Item criado com sucesso", stockService.saveStockItem(stockItem)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar item do estoque", description = "Atualiza um item existente. Somente ADMIN.")
    public ResponseEntity<ApiResponse<StockItem>> updateStockItem(
            @Parameter(description = "ID do item", example = "1") @PathVariable Long id,
            @RequestBody StockItem stockItem) {
        stockItem.setId(id);
        return ResponseEntity.ok(ApiResponse.success("Item atualizado", stockService.saveStockItem(stockItem)));
    }

    @PatchMapping("/{id}/quantity")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar quantidade", description = "Atualiza apenas a quantidade de um item. Retorna 404 se não encontrado.")
    public ResponseEntity<ApiResponse<StockItem>> updateQuantity(
            @Parameter(description = "ID do item", example = "1") @PathVariable Long id,
            @Parameter(description = "Nova quantidade", example = "50") @RequestParam Integer quantity) {
        return ResponseEntity.ok(ApiResponse.success("Quantidade atualizada", stockService.updateQuantity(id, quantity)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar item do estoque", description = "Remove permanentemente um item. Somente ADMIN.")
    public ResponseEntity<Void> deleteStockItem(
            @Parameter(description = "ID do item", example = "1") @PathVariable Long id) {
        stockService.deleteStockItem(id);
        return ResponseEntity.noContent().build();
    }
}
