package com.tiagoportilho.ServEasy.controller.api;

import com.tiagoportilho.ServEasy.dto.ApiResponse;
import com.tiagoportilho.ServEasy.model.Order;
import com.tiagoportilho.ServEasy.model.Sale;
import com.tiagoportilho.ServEasy.service.FeedbackService;
import com.tiagoportilho.ServEasy.service.OrderService;
import com.tiagoportilho.ServEasy.service.SaleService;
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
public class DashboardController {

    private final OrderService orderService;
    private final FeedbackService feedbackService;
    private final SaleService saleService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStats>> getDashboardStats() {
        try {
            // Buscar pedidos ativos (que ainda não foram fechados/convertidos em vendas)
            List<Order> todaysOrders = orderService.getTodaysOrders();
            
            // Buscar vendas do dia (contas fechadas) - ÚNICA fonte de receita
            List<Sale> todaysSales = saleService.getTodaysSales();
            BigDecimal totalRevenue = saleService.getTodaysTotalSales(); // Só dinheiro de contas fechadas
            
            // VENDAS: Cada pedido individual entregue conta como 1 venda
            // - Pedidos das contas fechadas (soma dos itemsCount das vendas)  
            int salesOrdersCount = todaysSales.stream()
                    .mapToInt(sale -> sale.getItemsCount() != null ? sale.getItemsCount() : 0)
                    .sum();
            
            int totalOrders = salesOrdersCount; // Só contar pedidos de contas fechadas como vendas
            
            // CLIENTES: Cada conta fechada = 1 cliente
            int uniqueCustomers = todaysSales.size();
            
            // CANCELAMENTOS: Pedidos ativos cancelados + pedidos cancelados nas vendas fechadas
            long activeCancelledOrders = todaysOrders.stream()
                    .filter(order -> order.getStatus() == Order.OrderStatus.CANCELADO)
                    .count();
            
            Long salesCancelledOrders = saleService.getTodaysCancelledOrders();
            long totalCancelledOrders = activeCancelledOrders + salesCancelledOrders;
            
            double averageRating = feedbackService.getAverageRating();
            
            // Calcular ticket médio baseado APENAS nas vendas fechadas
            BigDecimal averageTicket = totalOrders > 0 ? 
                    totalRevenue.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP) : 
                    BigDecimal.ZERO;

            DashboardStats stats = new DashboardStats();
            stats.setTotalOrders(totalOrders);           // Só pedidos de contas fechadas
            stats.setTotalRevenue(totalRevenue);         // Só dinheiro de contas fechadas
            stats.setUniqueCustomers(uniqueCustomers);   // Só contas fechadas
            stats.setCancelledOrders((int) totalCancelledOrders);
            stats.setAverageRating(averageRating);
            stats.setAverageTicket(averageTicket);       // Baseado só em vendas fechadas
            stats.setActiveOrders(todaysOrders.size() - (int) activeCancelledOrders); // Pedidos ativos (não cancelados)
            stats.setClosedAccounts(todaysSales.size()); // Contas fechadas hoje
            
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
        private int totalOrders;           // Total de pedidos do dia (ativos + fechados)
        private BigDecimal totalRevenue;   // Receita total (pedidos ativos + vendas fechadas)
        private int uniqueCustomers;       // Clientes únicos
        private int cancelledOrders;       // Pedidos cancelados
        private double averageRating;      // Avaliação média
        private BigDecimal averageTicket;  // Ticket médio
        private int activeOrders;          // Pedidos ainda ativos (não fechados)
        private int closedAccounts;        // Contas fechadas hoje
    }
}