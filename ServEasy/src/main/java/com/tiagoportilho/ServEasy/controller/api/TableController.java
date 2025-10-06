package com.tiagoportilho.ServEasy.controller.api;

import com.tiagoportilho.ServEasy.dto.ApiResponse;
import com.tiagoportilho.ServEasy.model.RestaurantTable;
import com.tiagoportilho.ServEasy.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TableController {

    private final TableService tableService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RestaurantTable>>> getAllTables() {
        try {
            List<RestaurantTable> tables = tableService.getAllTables();
            return ResponseEntity.ok(ApiResponse.success(tables));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao buscar mesas: " + e.getMessage()));
        }
    }

    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<RestaurantTable>>> getAvailableTables() {
        try {
            List<RestaurantTable> tables = tableService.getAvailableTables();
            return ResponseEntity.ok(ApiResponse.success(tables));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao buscar mesas disponíveis: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RestaurantTable>> getTableById(@PathVariable Long id) {
        try {
            Optional<RestaurantTable> table = tableService.getTableById(id);
            if (table.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success(table.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao buscar mesa: " + e.getMessage()));
        }
    }

    @GetMapping("/number/{tableNumber}")
    public ResponseEntity<ApiResponse<RestaurantTable>> getTableByNumber(@PathVariable Integer tableNumber) {
        try {
            Optional<RestaurantTable> table = tableService.getTableByNumber(tableNumber);
            if (table.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success(table.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao buscar mesa: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RestaurantTable>> createTable(@RequestBody RestaurantTable table) {
        try {
            RestaurantTable savedTable = tableService.saveTable(table);
            return ResponseEntity.ok(ApiResponse.success("Mesa criada com sucesso", savedTable));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao criar mesa: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RestaurantTable>> updateTable(@PathVariable Long id, @RequestBody RestaurantTable table) {
        try {
            table.setId(id);
            RestaurantTable updatedTable = tableService.saveTable(table);
            return ResponseEntity.ok(ApiResponse.success("Mesa atualizada com sucesso", updatedTable));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao atualizar mesa: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTable(@PathVariable Long id) {
        try {
            tableService.deleteTable(id);
            return ResponseEntity.ok(ApiResponse.success("Mesa deletada com sucesso", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao deletar mesa: " + e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<RestaurantTable>> updateTableStatus(
            @PathVariable Long id, 
            @RequestParam RestaurantTable.TableStatus status) {
        try {
            RestaurantTable updatedTable = tableService.updateTableStatus(id, status);
            return ResponseEntity.ok(ApiResponse.success("Status da mesa atualizado", updatedTable));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao atualizar status: " + e.getMessage()));
        }
    }

    @PatchMapping("/number/{tableNumber}/status")
    public ResponseEntity<ApiResponse<RestaurantTable>> updateTableStatusByNumber(
            @PathVariable Integer tableNumber, 
            @RequestParam RestaurantTable.TableStatus status) {
        try {
            RestaurantTable updatedTable = tableService.updateTableStatusByNumber(tableNumber, status);
            return ResponseEntity.ok(ApiResponse.success("Status da mesa atualizado", updatedTable));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao atualizar status: " + e.getMessage()));
        }
    }
}