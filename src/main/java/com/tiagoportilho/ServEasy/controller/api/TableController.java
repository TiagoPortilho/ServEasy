package com.tiagoportilho.ServEasy.controller.api;

import com.tiagoportilho.ServEasy.dto.ApiResponse;
import com.tiagoportilho.ServEasy.exception.BusinessException;
import com.tiagoportilho.ServEasy.exception.ResourceNotFoundException;
import com.tiagoportilho.ServEasy.model.Order;
import com.tiagoportilho.ServEasy.model.RestaurantTable;
import com.tiagoportilho.ServEasy.model.Sale;
import com.tiagoportilho.ServEasy.service.OrderService;
import com.tiagoportilho.ServEasy.service.SaleService;
import com.tiagoportilho.ServEasy.service.TableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
@Tag(name = "Mesas", description = "Gestão de mesas: disponibilidade, ocupação e fechamento de conta")
public class TableController {

    private final TableService tableService;
    private final OrderService orderService;
    private final SaleService saleService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE_ATENDENTE')")
    @Operation(summary = "Listar mesas", description = "Retorna todas as mesas com seus status atuais.")
    public ResponseEntity<ApiResponse<List<RestaurantTable>>> getAllTables() {
        return ResponseEntity.ok(ApiResponse.success(tableService.getAllTables()));
    }

    @GetMapping("/available")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE_ATENDENTE')")
    @Operation(summary = "Mesas disponíveis", description = "Retorna apenas as mesas com status DISPONIVEL.")
    public ResponseEntity<ApiResponse<List<RestaurantTable>>> getAvailableTables() {
        return ResponseEntity.ok(ApiResponse.success(tableService.getAvailableTables()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE_ATENDENTE')")
    @Operation(summary = "Buscar mesa por ID", description = "Retorna uma mesa específica. Retorna 404 se não encontrada.")
    public ResponseEntity<ApiResponse<RestaurantTable>> getTableById(
            @Parameter(description = "ID da mesa", example = "1") @PathVariable Long id) {
        RestaurantTable table = tableService.getTableById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa", id));
        return ResponseEntity.ok(ApiResponse.success(table));
    }

    @GetMapping("/number/{tableNumber}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE_ATENDENTE')")
    @Operation(summary = "Buscar mesa por número", description = "Retorna uma mesa pelo seu número. Retorna 404 se não encontrada.")
    public ResponseEntity<ApiResponse<RestaurantTable>> getTableByNumber(
            @Parameter(description = "Número da mesa", example = "5") @PathVariable Integer tableNumber) {
        RestaurantTable table = tableService.getTableByNumber(tableNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa", "número " + tableNumber));
        return ResponseEntity.ok(ApiResponse.success(table));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar mesa", description = "Cria uma nova mesa. Retorna 409 se o número já existir. Somente ADMIN.")
    public ResponseEntity<ApiResponse<RestaurantTable>> createTable(@RequestBody RestaurantTable table) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Mesa criada com sucesso", tableService.saveTable(table)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar mesa", description = "Atualiza dados de uma mesa. Somente ADMIN.")
    public ResponseEntity<ApiResponse<RestaurantTable>> updateTable(
            @Parameter(description = "ID da mesa", example = "1") @PathVariable Long id,
            @RequestBody RestaurantTable table) {
        table.setId(id);
        return ResponseEntity.ok(ApiResponse.success("Mesa atualizada", tableService.saveTable(table)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar mesa", description = "Remove permanentemente uma mesa. Somente ADMIN.")
    public ResponseEntity<Void> deleteTable(
            @Parameter(description = "ID da mesa", example = "1") @PathVariable Long id) {
        tableService.deleteTable(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE_ATENDENTE')")
    @Operation(summary = "Atualizar status da mesa por ID", description = "Altera o status da mesa (DISPONIVEL, OCUPADA, RESERVADA, MANUTENCAO).")
    public ResponseEntity<ApiResponse<RestaurantTable>> updateTableStatus(
            @Parameter(description = "ID da mesa", example = "1") @PathVariable Long id,
            @Parameter(description = "Novo status", example = "OCUPADA") @RequestParam RestaurantTable.TableStatus status) {
        return ResponseEntity.ok(ApiResponse.success("Status da mesa atualizado", tableService.updateTableStatus(id, status)));
    }

    @PatchMapping("/number/{tableNumber}/status")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE_ATENDENTE')")
    @Operation(summary = "Atualizar status da mesa por número", description = "Altera o status da mesa pelo número.")
    public ResponseEntity<ApiResponse<RestaurantTable>> updateTableStatusByNumber(
            @Parameter(description = "Número da mesa", example = "5") @PathVariable Integer tableNumber,
            @Parameter(description = "Novo status", example = "OCUPADA") @RequestParam RestaurantTable.TableStatus status) {
        return ResponseEntity.ok(ApiResponse.success("Status da mesa atualizado",
                tableService.updateTableStatusByNumber(tableNumber, status)));
    }

    @PostMapping("/occupy/{tableNumber}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE_ATENDENTE')")
    @Operation(summary = "Ocupar mesa", description = "Marca a mesa como OCUPADA. Retorna 409 se já ocupada.")
    public ResponseEntity<ApiResponse<RestaurantTable>> occupyTable(
            @Parameter(description = "Número da mesa", example = "5") @PathVariable Integer tableNumber,
            @RequestParam(required = false) String customerName) {
        RestaurantTable table = tableService.getTableByNumber(tableNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa", "número " + tableNumber));

        if (table.getStatus() == RestaurantTable.TableStatus.OCUPADA) {
            throw new BusinessException("Mesa já está ocupada", HttpStatus.CONFLICT, "TABLE_ALREADY_OCCUPIED");
        }
        if (table.getStatus() == RestaurantTable.TableStatus.RESERVADA
                && (customerName == null || customerName.isBlank())) {
            throw new BusinessException("Mesa reservada. Informe o nome do cliente para confirmar.",
                    HttpStatus.BAD_REQUEST, "CUSTOMER_NAME_REQUIRED");
        }

        return ResponseEntity.ok(ApiResponse.success("Mesa ocupada com sucesso",
                tableService.updateTableStatus(table.getId(), RestaurantTable.TableStatus.OCUPADA)));
    }

    @PostMapping("/release/{tableNumber}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE_ATENDENTE')")
    @Operation(summary = "Liberar mesa", description = "Marca a mesa como DISPONIVEL. Retorna 409 se a mesa não estiver ocupada.")
    public ResponseEntity<ApiResponse<RestaurantTable>> releaseTable(
            @Parameter(description = "Número da mesa", example = "5") @PathVariable Integer tableNumber) {
        RestaurantTable table = tableService.getTableByNumber(tableNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa", "número " + tableNumber));

        if (table.getStatus() != RestaurantTable.TableStatus.OCUPADA) {
            throw new BusinessException("Mesa não está ocupada", HttpStatus.CONFLICT, "TABLE_NOT_OCCUPIED");
        }

        return ResponseEntity.ok(ApiResponse.success("Mesa liberada com sucesso",
                tableService.updateTableStatus(table.getId(), RestaurantTable.TableStatus.DISPONIVEL)));
    }

    @PostMapping("/close-account/{tableNumber}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE_ATENDENTE')")
    @Operation(summary = "Fechar conta da mesa",
            description = "Fecha a conta, registra a venda e libera a mesa. Retorna 409 se houver pedidos prontos aguardando entrega.")
    public ResponseEntity<ApiResponse<Object>> closeTableAccount(
            @Parameter(description = "Número da mesa", example = "5") @PathVariable Integer tableNumber) {
        RestaurantTable table = tableService.getTableByNumber(tableNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa", "número " + tableNumber));

        if (table.getStatus() != RestaurantTable.TableStatus.OCUPADA) {
            throw new BusinessException("Mesa não está ocupada", HttpStatus.CONFLICT, "TABLE_NOT_OCCUPIED");
        }

        List<Order> pedidosDaMesa = orderService.getOrdersByTable(tableNumber);

        if (pedidosDaMesa.isEmpty()) {
            tableService.updateTableStatus(table.getId(), RestaurantTable.TableStatus.DISPONIVEL);
            return ResponseEntity.ok(ApiResponse.success("Mesa liberada — nenhum pedido encontrado", null));
        }

        List<Order> pedidosProntos = pedidosDaMesa.stream()
                .filter(p -> p.getStatus() == Order.OrderStatus.PRONTO).toList();

        if (!pedidosProntos.isEmpty()) {
            throw new BusinessException(
                    "Não é possível fechar a conta. Há " + pedidosProntos.size() + " pedido(s) pronto(s) aguardando entrega.",
                    HttpStatus.CONFLICT, "PENDING_DELIVERY");
        }

        List<Order> pedidosEntregues = pedidosDaMesa.stream()
                .filter(p -> p.getStatus() == Order.OrderStatus.ENTREGUE).toList();

        List<Order> pedidosAtivos = pedidosDaMesa.stream()
                .filter(p -> p.getStatus() == Order.OrderStatus.NOVO
                        || p.getStatus() == Order.OrderStatus.EM_ANDAMENTO).toList();

        BigDecimal totalCobrar = pedidosEntregues.stream()
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalItens = pedidosEntregues.stream()
                .mapToInt(p -> p.getItems() != null ? p.getItems().size() : 0)
                .sum();

        if (totalCobrar.compareTo(BigDecimal.ZERO) > 0) {
            Sale sale = new Sale();
            sale.setTableNumber(tableNumber);
            sale.setTotalAmount(totalCobrar);
            sale.setItemsCount(totalItens);
            sale.setOrderIds(pedidosEntregues.stream()
                    .map(p -> p.getId().toString())
                    .reduce((a, b) -> a + "," + b).orElse(""));
            sale.setPaymentMethod("DINHEIRO");
            sale.setCancelledOrdersCount(pedidosAtivos.size());
            saleService.saveSale(sale);
        }

        pedidosDaMesa.forEach(p -> orderService.deleteOrder(p.getId()));
        tableService.updateTableStatus(table.getId(), RestaurantTable.TableStatus.DISPONIVEL);

        String aviso = pedidosAtivos.isEmpty() ? "" : pedidosAtivos.size() + " pedido(s) em preparo cancelado(s). ";
        return ResponseEntity.ok(ApiResponse.success(
                aviso + "Conta fechada. Total: R$ " + totalCobrar,
                Map.of("totalCobrado", totalCobrar,
                        "pedidosEntregues", pedidosEntregues.size(),
                        "pedidosCancelados", pedidosAtivos.size())));
    }
}
