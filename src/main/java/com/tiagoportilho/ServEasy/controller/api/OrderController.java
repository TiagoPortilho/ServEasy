package com.tiagoportilho.ServEasy.controller.api;

import com.tiagoportilho.ServEasy.dto.ApiResponse;
import com.tiagoportilho.ServEasy.dto.OrderRequest;
import com.tiagoportilho.ServEasy.dto.OrderResponseDTO;
import com.tiagoportilho.ServEasy.dto.request.StatusUpdateRequest;
import com.tiagoportilho.ServEasy.exception.ResourceNotFoundException;
import com.tiagoportilho.ServEasy.exception.ValidationException;
import com.tiagoportilho.ServEasy.model.Order;
import com.tiagoportilho.ServEasy.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Ciclo de vida dos pedidos: criação, acompanhamento e encerramento")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','COZINHEIRO','CLIENTE_ATENDENTE')")
    @Operation(summary = "Listar pedidos", description = "Retorna todos os pedidos ou filtra por status (query param). Acessível por todos os papéis.")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getAllOrders(
            @Parameter(description = "Filtrar por status: NOVO, EM_ANDAMENTO, PRONTO, ENTREGUE, CANCELADO", example = "NOVO")
            @RequestParam(required = false) String status) {
        List<Order> orders;
        if (status != null && !status.isEmpty()) {
            try {
                Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
                orders = orderService.getOrdersByStatus(orderStatus);
            } catch (IllegalArgumentException e) {
                throw new ValidationException("status", "Status inválido: " + status);
            }
        } else {
            orders = orderService.getAllOrders();
        }
        return ResponseEntity.ok(ApiResponse.success(orders.stream().map(OrderResponseDTO::fromEntity).toList()));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN','COZINHEIRO','CLIENTE_ATENDENTE')")
    @Operation(summary = "Buscar por status", description = "Retorna pedidos com o status especificado.")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getOrdersByStatus(
            @Parameter(description = "Status do pedido") @PathVariable Order.OrderStatus status) {
        return ResponseEntity.ok(ApiResponse.success(
                orderService.getOrdersByStatus(status).stream().map(OrderResponseDTO::fromEntity).toList()));
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN','COZINHEIRO')")
    @Operation(summary = "Pedidos novos", description = "Retorna pedidos com status NOVO. Uso exclusivo da cozinha e admin.")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getNewOrders() {
        return ResponseEntity.ok(ApiResponse.success(
                orderService.getNewOrders().stream().map(OrderResponseDTO::fromEntity).toList()));
    }

    @GetMapping("/in-progress")
    @PreAuthorize("hasAnyRole('ADMIN','COZINHEIRO')")
    @Operation(summary = "Pedidos em andamento", description = "Retorna pedidos com status EM_ANDAMENTO.")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getOrdersInProgress() {
        return ResponseEntity.ok(ApiResponse.success(
                orderService.getOrdersInProgress().stream().map(OrderResponseDTO::fromEntity).toList()));
    }

    @GetMapping("/ready")
    @PreAuthorize("hasAnyRole('ADMIN','COZINHEIRO','CLIENTE_ATENDENTE')")
    @Operation(summary = "Pedidos prontos", description = "Retorna pedidos com status PRONTO aguardando entrega.")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getReadyOrders() {
        return ResponseEntity.ok(ApiResponse.success(
                orderService.getReadyOrders().stream().map(OrderResponseDTO::fromEntity).toList()));
    }

    @GetMapping("/today")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Pedidos de hoje", description = "Retorna todos os pedidos criados hoje. Somente ADMIN.")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getTodaysOrders() {
        return ResponseEntity.ok(ApiResponse.success(
                orderService.getTodaysOrders().stream().map(OrderResponseDTO::fromEntity).toList()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','COZINHEIRO','CLIENTE_ATENDENTE')")
    @Operation(summary = "Buscar pedido por ID", description = "Retorna um pedido específico. Retorna 404 se não encontrado.")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> getOrderById(
            @Parameter(description = "ID do pedido", example = "42") @PathVariable Long id) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));
        return ResponseEntity.ok(ApiResponse.success(OrderResponseDTO.fromEntity(order)));
    }

    @GetMapping("/table/{tableNumber}")
    @PreAuthorize("hasAnyRole('ADMIN','COZINHEIRO','CLIENTE_ATENDENTE')")
    @Operation(summary = "Pedidos por mesa", description = "Retorna todos os pedidos de uma mesa específica.")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getOrdersByTable(
            @Parameter(description = "Número da mesa", example = "5") @PathVariable Integer tableNumber) {
        return ResponseEntity.ok(ApiResponse.success(
                orderService.getOrdersByTable(tableNumber).stream().map(OrderResponseDTO::fromEntity).toList()));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE_ATENDENTE')")
    @Operation(summary = "Criar pedido",
            description = "Cria um novo pedido para uma mesa OCUPADA. Requer CLIENTE_ATENDENTE ou ADMIN. " +
                    "Retorna 409 se a mesa não estiver ocupada ou o item não estiver disponível.")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        Order created = orderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Pedido criado com sucesso", OrderResponseDTO.fromEntity(created)));
    }

    @PatchMapping("/{id}/start")
    @PreAuthorize("hasAnyRole('ADMIN','COZINHEIRO')")
    @Operation(summary = "Iniciar preparo", description = "Transição NOVO → EM_ANDAMENTO. Requer COZINHEIRO ou ADMIN.")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> startOrder(
            @Parameter(description = "ID do pedido", example = "42") @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Pedido iniciado",
                OrderResponseDTO.fromEntity(orderService.markAsInProgress(id))));
    }

    @PatchMapping("/{id}/ready")
    @PreAuthorize("hasAnyRole('ADMIN','COZINHEIRO')")
    @Operation(summary = "Marcar como pronto", description = "Transição EM_ANDAMENTO → PRONTO. Requer COZINHEIRO ou ADMIN.")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> markAsReady(
            @Parameter(description = "ID do pedido", example = "42") @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Pedido pronto",
                OrderResponseDTO.fromEntity(orderService.markAsReady(id))));
    }

    @PatchMapping("/{id}/deliver")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE_ATENDENTE')")
    @Operation(summary = "Marcar como entregue", description = "Transição PRONTO → ENTREGUE. Requer CLIENTE_ATENDENTE ou ADMIN.")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> markAsDelivered(
            @Parameter(description = "ID do pedido", example = "42") @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Pedido entregue",
                OrderResponseDTO.fromEntity(orderService.markAsDelivered(id))));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN','COZINHEIRO','CLIENTE_ATENDENTE')")
    @Operation(summary = "Cancelar pedido", description = "Cancela o pedido. Retorna 409 se já entregue ou cancelado.")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(
            @Parameter(description = "ID do pedido", example = "42") @PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Pedido cancelado", null));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','COZINHEIRO','CLIENTE_ATENDENTE')")
    @Operation(summary = "Atualizar status via body",
            description = "Atualiza o status do pedido. Body: {\"status\": \"EM_ANDAMENTO\"}. Retorna 409 para transições inválidas.")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> updateOrderStatus(
            @Parameter(description = "ID do pedido", example = "42") @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request) {
        try {
            Order.OrderStatus newStatus = Order.OrderStatus.valueOf(request.getStatus().toUpperCase());
            return ResponseEntity.ok(ApiResponse.success("Status atualizado",
                    OrderResponseDTO.fromEntity(orderService.updateOrderStatus(id, newStatus))));
        } catch (IllegalArgumentException e) {
            throw new ValidationException("status", "Status inválido: " + request.getStatus());
        }
    }
}
