package com.tiagoportilho.ServEasy.controller.api;

import com.tiagoportilho.ServEasy.dto.ApiResponse;
import com.tiagoportilho.ServEasy.model.Order;
import com.tiagoportilho.ServEasy.model.RestaurantTable;
import com.tiagoportilho.ServEasy.model.Sale;
import com.tiagoportilho.ServEasy.service.OrderService;
import com.tiagoportilho.ServEasy.service.SaleService;
import com.tiagoportilho.ServEasy.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TableController {

    private final TableService tableService;
    private final OrderService orderService;
    private final SaleService saleService;

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

    @PostMapping("/occupy/{tableNumber}")
    public ResponseEntity<ApiResponse<RestaurantTable>> occupyTable(
            @PathVariable Integer tableNumber,
            @RequestParam(required = false) String customerName) {
        try {
            Optional<RestaurantTable> tableOpt = tableService.getTableByNumber(tableNumber);
            if (tableOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            RestaurantTable table = tableOpt.get();
            
            if (table.getStatus() == RestaurantTable.TableStatus.OCUPADA) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Mesa já está ocupada"));
            }
            
            if (table.getStatus() == RestaurantTable.TableStatus.RESERVADA) {
                // Se está reservada, precisa confirmar com nome do cliente
                if (customerName == null || customerName.trim().isEmpty()) {
                    return ResponseEntity.badRequest()
                            .body(ApiResponse.error("Mesa reservada. Informe o nome do cliente para confirmar a reserva."));
                }
                // Aqui poderia validar o nome com o sistema de reservas
                // Por agora, vamos apenas aceitar qualquer nome não vazio
            }
            
            RestaurantTable updatedTable = tableService.updateTableStatus(table.getId(), RestaurantTable.TableStatus.OCUPADA);
            return ResponseEntity.ok(ApiResponse.success("Mesa ocupada com sucesso", updatedTable));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao ocupar mesa: " + e.getMessage()));
        }
    }

    @PostMapping("/release/{tableNumber}")
    public ResponseEntity<ApiResponse<RestaurantTable>> releaseTable(@PathVariable Integer tableNumber) {
        try {
            Optional<RestaurantTable> tableOpt = tableService.getTableByNumber(tableNumber);
            if (tableOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            RestaurantTable table = tableOpt.get();
            
            if (table.getStatus() != RestaurantTable.TableStatus.OCUPADA) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Mesa não está ocupada"));
            }
            
            RestaurantTable updatedTable = tableService.updateTableStatus(table.getId(), RestaurantTable.TableStatus.DISPONIVEL);
            return ResponseEntity.ok(ApiResponse.success("Mesa liberada com sucesso", updatedTable));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao liberar mesa: " + e.getMessage()));
        }
    }

    @PostMapping("/close-account/{tableNumber}")
    public ResponseEntity<ApiResponse<Object>> closeTableAccount(@PathVariable Integer tableNumber) {
        try {
            Optional<RestaurantTable> tableOpt = tableService.getTableByNumber(tableNumber);
            if (tableOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            RestaurantTable table = tableOpt.get();
            
            if (table.getStatus() != RestaurantTable.TableStatus.OCUPADA) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Mesa não está ocupada"));
            }

            // Buscar todos os pedidos da mesa
            List<Order> pedidosDaMesa = orderService.getOrdersByTable(tableNumber);
            
            if (pedidosDaMesa.isEmpty()) {
                // Não há pedidos, apenas liberar a mesa
                tableService.updateTableStatus(table.getId(), RestaurantTable.TableStatus.DISPONIVEL);
                return ResponseEntity.ok(ApiResponse.success("Mesa liberada - nenhum pedido encontrado", null));
            }

            // Verificar status dos pedidos
            List<Order> pedidosProntos = pedidosDaMesa.stream()
                    .filter(p -> p.getStatus() == Order.OrderStatus.PRONTO)
                    .collect(Collectors.toList());

            List<Order> pedidosEntregues = pedidosDaMesa.stream()
                    .filter(p -> p.getStatus() == Order.OrderStatus.ENTREGUE)
                    .collect(Collectors.toList());

            List<Order> pedidosAtivos = pedidosDaMesa.stream()
                    .filter(p -> p.getStatus() == Order.OrderStatus.NOVO || p.getStatus() == Order.OrderStatus.EM_ANDAMENTO)
                    .collect(Collectors.toList());

            // REGRA: Não permitir fechar se há pedidos PRONTOS (aguardando entrega)
            if (!pedidosProntos.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Não é possível fechar a conta. Há " + pedidosProntos.size() + " pedido(s) pronto(s) aguardando entrega."));
            }

            // Calcular total apenas dos pedidos ENTREGUES
            BigDecimal totalCobrar = pedidosEntregues.stream()
                    .map(Order::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            int totalItens = pedidosEntregues.stream()
                    .mapToInt(p -> p.getItems() != null ? p.getItems().size() : 0)
                    .sum();

            // Se há pedidos ativos, retornar aviso
            String avisoAtivos = "";
            if (!pedidosAtivos.isEmpty()) {
                avisoAtivos = pedidosAtivos.size() + " pedido(s) em preparo será(ão) cancelado(s). ";
            }

            // Criar venda no sistema
            if (totalCobrar.compareTo(BigDecimal.ZERO) > 0) {
                Sale sale = new Sale();
                sale.setTableNumber(tableNumber);
                sale.setTotalAmount(totalCobrar);
                sale.setItemsCount(totalItens);
                sale.setOrderIds(pedidosEntregues.stream()
                        .map(p -> p.getId().toString())
                        .collect(Collectors.joining(",")));
                sale.setPaymentMethod("DINHEIRO"); // Default - poderia ser parametrizado
                sale.setCancelledOrdersCount(pedidosAtivos.size()); // Registrar quantos foram cancelados
                
                saleService.saveSale(sale);
            }

            // Deletar TODOS os pedidos da mesa
            for (Order pedido : pedidosDaMesa) {
                orderService.deleteOrder(pedido.getId());
            }

            // Liberar mesa
            tableService.updateTableStatus(table.getId(), RestaurantTable.TableStatus.DISPONIVEL);

            String mensagem = avisoAtivos + "Conta fechada com sucesso. Total cobrado: R$ " + totalCobrar.toString();
            
            return ResponseEntity.ok(ApiResponse.success(mensagem, Map.of(
                    "totalCobrado", totalCobrar,
                    "pedidosEntregues", pedidosEntregues.size(),
                    "pedidosCancelados", pedidosAtivos.size()
            )));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao fechar conta: " + e.getMessage()));
        }
    }
}