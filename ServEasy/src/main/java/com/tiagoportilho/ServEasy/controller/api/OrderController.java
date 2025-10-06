package com.tiagoportilho.ServEasy.controller.api;

import com.tiagoportilho.ServEasy.dto.ApiResponse;
import com.tiagoportilho.ServEasy.dto.OrderRequest;
import com.tiagoportilho.ServEasy.model.Order;
import com.tiagoportilho.ServEasy.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrders() {
        try {
            List<Order> orders = orderService.getAllOrders();
            return ResponseEntity.ok(ApiResponse.success(orders));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao buscar pedidos: " + e.getMessage()));
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersByStatus(@PathVariable Order.OrderStatus status) {
        try {
            List<Order> orders = orderService.getOrdersByStatus(status);
            return ResponseEntity.ok(ApiResponse.success(orders));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao buscar pedidos por status: " + e.getMessage()));
        }
    }

    @GetMapping("/new")
    public ResponseEntity<ApiResponse<List<Order>>> getNewOrders() {
        try {
            List<Order> orders = orderService.getNewOrders();
            return ResponseEntity.ok(ApiResponse.success(orders));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao buscar novos pedidos: " + e.getMessage()));
        }
    }

    @GetMapping("/in-progress")
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersInProgress() {
        try {
            List<Order> orders = orderService.getOrdersInProgress();
            return ResponseEntity.ok(ApiResponse.success(orders));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao buscar pedidos em andamento: " + e.getMessage()));
        }
    }

    @GetMapping("/ready")
    public ResponseEntity<ApiResponse<List<Order>>> getReadyOrders() {
        try {
            List<Order> orders = orderService.getReadyOrders();
            return ResponseEntity.ok(ApiResponse.success(orders));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao buscar pedidos prontos: " + e.getMessage()));
        }
    }

    @GetMapping("/today")
    public ResponseEntity<ApiResponse<List<Order>>> getTodaysOrders() {
        try {
            List<Order> orders = orderService.getTodaysOrders();
            return ResponseEntity.ok(ApiResponse.success(orders));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao buscar pedidos de hoje: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Order>> getOrderById(@PathVariable Long id) {
        try {
            Optional<Order> order = orderService.getOrderById(id);
            if (order.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success(order.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao buscar pedido: " + e.getMessage()));
        }
    }

    @GetMapping("/table/{tableNumber}")
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersByTable(@PathVariable Integer tableNumber) {
        try {
            List<Order> orders = orderService.getOrdersByTable(tableNumber);
            return ResponseEntity.ok(ApiResponse.success(orders));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao buscar pedidos da mesa: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Order>> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            Order createdOrder = orderService.createOrder(orderRequest);
            return ResponseEntity.ok(ApiResponse.success("Pedido criado com sucesso", createdOrder));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao criar pedido: " + e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Order>> updateOrderStatus(
            @PathVariable Long id, 
            @RequestParam Order.OrderStatus status) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(ApiResponse.success("Status atualizado com sucesso", updatedOrder));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao atualizar status: " + e.getMessage()));
        }
    }

    @PatchMapping("/{id}/start")
    public ResponseEntity<ApiResponse<Order>> startOrder(@PathVariable Long id) {
        try {
            Order updatedOrder = orderService.markAsInProgress(id);
            return ResponseEntity.ok(ApiResponse.success("Pedido iniciado", updatedOrder));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao iniciar pedido: " + e.getMessage()));
        }
    }

    @PatchMapping("/{id}/ready")
    public ResponseEntity<ApiResponse<Order>> markAsReady(@PathVariable Long id) {
        try {
            Order updatedOrder = orderService.markAsReady(id);
            return ResponseEntity.ok(ApiResponse.success("Pedido marcado como pronto", updatedOrder));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao marcar como pronto: " + e.getMessage()));
        }
    }

    @PatchMapping("/{id}/deliver")
    public ResponseEntity<ApiResponse<Order>> markAsDelivered(@PathVariable Long id) {
        try {
            Order updatedOrder = orderService.markAsDelivered(id);
            return ResponseEntity.ok(ApiResponse.success("Pedido entregue", updatedOrder));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao marcar como entregue: " + e.getMessage()));
        }
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(@PathVariable Long id) {
        try {
            orderService.cancelOrder(id);
            return ResponseEntity.ok(ApiResponse.success("Pedido cancelado", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Erro ao cancelar pedido: " + e.getMessage()));
        }
    }
}