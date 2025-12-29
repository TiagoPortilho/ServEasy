package com.tiagoportilho.ServEasy.controller.api;

import com.tiagoportilho.ServEasy.dto.ApiResponse;
import com.tiagoportilho.ServEasy.dto.OrderRequest;
import com.tiagoportilho.ServEasy.dto.OrderResponseDTO;
import com.tiagoportilho.ServEasy.exception.ResourceNotFoundException;
import com.tiagoportilho.ServEasy.exception.ValidationException;
import com.tiagoportilho.ServEasy.model.Order;
import com.tiagoportilho.ServEasy.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller para gerenciamento de pedidos.
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Pedidos", description = "Gerenciamento de pedidos do restaurante")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Listar pedidos", description = "Retorna todos os pedidos ou filtra por status")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getAllOrders(
            @Parameter(description = "Filtrar por status") @RequestParam(required = false) String status) {
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
        
        List<OrderResponseDTO> orderDTOs = orders.stream()
                .map(OrderResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(orderDTOs));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Buscar por status", description = "Retorna pedidos com um status específico")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getOrdersByStatus(
            @Parameter(description = "Status do pedido") @PathVariable Order.OrderStatus status) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        List<OrderResponseDTO> orderDTOs = orders.stream()
                .map(OrderResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(orderDTOs));
    }

    @GetMapping("/new")
    @Operation(summary = "Pedidos novos", description = "Retorna todos os pedidos com status NOVO")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getNewOrders() {
        List<Order> orders = orderService.getNewOrders();
        List<OrderResponseDTO> orderDTOs = orders.stream()
                .map(OrderResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(orderDTOs));
    }

    @GetMapping("/in-progress")
    @Operation(summary = "Pedidos em andamento", description = "Retorna todos os pedidos com status EM_ANDAMENTO")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getOrdersInProgress() {
        List<Order> orders = orderService.getOrdersInProgress();
        List<OrderResponseDTO> orderDTOs = orders.stream()
                .map(OrderResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(orderDTOs));
    }

    @GetMapping("/ready")
    @Operation(summary = "Pedidos prontos", description = "Retorna todos os pedidos com status PRONTO")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getReadyOrders() {
        List<Order> orders = orderService.getReadyOrders();
        List<OrderResponseDTO> orderDTOs = orders.stream()
                .map(OrderResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(orderDTOs));
    }

    @GetMapping("/today")
    @Operation(summary = "Pedidos de hoje", description = "Retorna todos os pedidos criados hoje")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getTodaysOrders() {
        List<Order> orders = orderService.getTodaysOrders();
        List<OrderResponseDTO> orderDTOs = orders.stream()
                .map(OrderResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(orderDTOs));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Retorna um pedido específico")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> getOrderById(
            @Parameter(description = "ID do pedido") @PathVariable Long id) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));
        OrderResponseDTO orderDTO = OrderResponseDTO.fromEntity(order);
        return ResponseEntity.ok(ApiResponse.success(orderDTO));
    }

    @GetMapping("/table/{tableNumber}")
    @Operation(summary = "Pedidos por mesa", description = "Retorna todos os pedidos de uma mesa específica")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getOrdersByTable(
            @Parameter(description = "Número da mesa") @PathVariable Integer tableNumber) {
        List<Order> orders = orderService.getOrdersByTable(tableNumber);
        List<OrderResponseDTO> orderDTOs = orders.stream()
            .map(OrderResponseDTO::fromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(orderDTOs));
    }

    @PostMapping
    @Operation(summary = "Criar pedido", description = "Cria um novo pedido")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> createOrder(@RequestBody OrderRequest orderRequest) {
        Order createdOrder = orderService.createOrder(orderRequest);
        OrderResponseDTO orderDTO = OrderResponseDTO.fromEntity(createdOrder);
        log.info("Pedido criado com ID: {}", createdOrder.getId());
        return ResponseEntity.ok(ApiResponse.success("Pedido criado com sucesso", orderDTO));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status", description = "Atualiza o status de um pedido")
    public ResponseEntity<ApiResponse<Order>> updateOrderStatus(
            @Parameter(description = "ID do pedido") @PathVariable Long id, 
            @Parameter(description = "Novo status") @RequestParam Order.OrderStatus status) {
        Order updatedOrder = orderService.updateOrderStatus(id, status);
        log.info("Status do pedido {} atualizado para {}", id, status);
        return ResponseEntity.ok(ApiResponse.success("Status atualizado com sucesso", updatedOrder));
    }

    @PatchMapping("/{id}/start")
    @Operation(summary = "Iniciar pedido", description = "Marca o pedido como em andamento")
    public ResponseEntity<ApiResponse<Order>> startOrder(
            @Parameter(description = "ID do pedido") @PathVariable Long id) {
        Order updatedOrder = orderService.markAsInProgress(id);
        log.info("Pedido {} iniciado", id);
        return ResponseEntity.ok(ApiResponse.success("Pedido iniciado", updatedOrder));
    }

    @PatchMapping("/{id}/ready")
    @Operation(summary = "Marcar como pronto", description = "Marca o pedido como pronto para entrega")
    public ResponseEntity<ApiResponse<Order>> markAsReady(
            @Parameter(description = "ID do pedido") @PathVariable Long id) {
        Order updatedOrder = orderService.markAsReady(id);
        log.info("Pedido {} marcado como pronto", id);
        return ResponseEntity.ok(ApiResponse.success("Pedido marcado como pronto", updatedOrder));
    }

    @PatchMapping("/{id}/deliver")
    @Operation(summary = "Marcar como entregue", description = "Marca o pedido como entregue ao cliente")
    public ResponseEntity<ApiResponse<Order>> markAsDelivered(
            @Parameter(description = "ID do pedido") @PathVariable Long id) {
        Order updatedOrder = orderService.markAsDelivered(id);
        log.info("Pedido {} entregue", id);
        return ResponseEntity.ok(ApiResponse.success("Pedido entregue", updatedOrder));
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancelar pedido", description = "Cancela um pedido")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(
            @Parameter(description = "ID do pedido") @PathVariable Long id) {
        orderService.cancelOrder(id);
        log.info("Pedido {} cancelado", id);
        return ResponseEntity.ok(ApiResponse.success("Pedido cancelado", null));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Atualizar status (body)", description = "Atualiza o status de um pedido via body")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> updateOrderStatus(
            @Parameter(description = "ID do pedido") @PathVariable Long id, 
            @RequestBody StatusUpdateRequest request) {
        try {
            Order.OrderStatus newStatus = Order.OrderStatus.valueOf(request.getStatus().toUpperCase());
            Order updatedOrder = orderService.updateOrderStatus(id, newStatus);
            OrderResponseDTO responseDTO = OrderResponseDTO.fromEntity(updatedOrder);
            log.info("Status do pedido {} atualizado para {}", id, newStatus);
            return ResponseEntity.ok(ApiResponse.success("Status atualizado", responseDTO));
        } catch (IllegalArgumentException e) {
            throw new ValidationException("status", "Status inválido: " + request.getStatus());
        }
    }

    public static class StatusUpdateRequest {
        private String status;
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}