package com.tiagoportilho.ServEasy.controller.api;

import com.tiagoportilho.ServEasy.dto.ApiResponse;
import com.tiagoportilho.ServEasy.model.Order;
import com.tiagoportilho.ServEasy.service.FeedbackService;
import com.tiagoportilho.ServEasy.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    private final OrderService orderService;
    private final FeedbackService feedbackService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStats>> getDashboardStats() {
        try {
            List<Order> todaysOrders = orderService.getTodaysOrders();
            
            // Calcular estatísticas
            int totalOrders = todaysOrders.size();
            BigDecimal totalRevenue = todaysOrders.stream()
                    .filter(order -> order.getStatus() != Order.OrderStatus.CANCELADO)
                    .map(Order::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            long uniqueCustomers = todaysOrders.stream()
                    .filter(order -> order.getCustomerName() != null && !order.getCustomerName().isEmpty())
                    .map(Order::getCustomerName)
                    .distinct()
                    .count();
            
            long cancelledOrders = todaysOrders.stream()
                    .filter(order -> order.getStatus() == Order.OrderStatus.CANCELADO)
                    .count();
            
            double averageRating = feedbackService.getAverageRating();
            
            BigDecimal averageTicket = totalOrders > 0 ? 
                    totalRevenue.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP) : 
                    BigDecimal.ZERO;

            DashboardStats stats = new DashboardStats();
            stats.setTotalOrders(totalOrders);
            stats.setTotalRevenue(totalRevenue);
            stats.setUniqueCustomers((int) uniqueCustomers);
            stats.setCancelledOrders((int) cancelledOrders);
            stats.setAverageRating(averageRating);
            stats.setAverageTicket(averageTicket);
            
            return ResponseEntity.ok(ApiResponse.success(stats));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro ao buscar estatísticas: " + e.getMessage()));
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardStats {
        private int totalOrders;
        private BigDecimal totalRevenue;
        private int uniqueCustomers;
        private int cancelledOrders;
        private double averageRating;
        private BigDecimal averageTicket;
    }
}