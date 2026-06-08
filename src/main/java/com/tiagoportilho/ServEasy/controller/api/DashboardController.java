package com.tiagoportilho.ServEasy.controller.api;

import com.tiagoportilho.ServEasy.dto.ApiResponse;
import com.tiagoportilho.ServEasy.dto.response.DashboardStatsResponse;
import com.tiagoportilho.ServEasy.model.Order;
import com.tiagoportilho.ServEasy.model.Sale;
import com.tiagoportilho.ServEasy.service.FeedbackService;
import com.tiagoportilho.ServEasy.service.OrderService;
import com.tiagoportilho.ServEasy.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Estatísticas em tempo real para admin e cozinha")
public class DashboardController {

    private final OrderService orderService;
    private final FeedbackService feedbackService;
    private final SaleService saleService;

    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN','COZINHEIRO','CLIENTE_ATENDENTE')")
    @Operation(summary = "Estatísticas do dia",
            description = "Retorna métricas do dia: pedidos, receita, clientes, cancelamentos e avaliação média.")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getDashboardStats() {
        List<Order> todaysOrders = orderService.getTodaysOrders();
        List<Sale> todaysSales = saleService.getTodaysSales();
        BigDecimal totalRevenue = saleService.getTodaysTotalSales();

        int salesOrdersCount = todaysSales.stream()
                .mapToInt(sale -> sale.getItemsCount() != null ? sale.getItemsCount() : 0)
                .sum();

        long activeCancelledOrders = todaysOrders.stream()
                .filter(order -> order.getStatus() == Order.OrderStatus.CANCELADO)
                .count();

        Long salesCancelledOrders = saleService.getTodaysCancelledOrders();
        long totalCancelledOrders = activeCancelledOrders + salesCancelledOrders;

        BigDecimal averageTicket = salesOrdersCount > 0
                ? totalRevenue.divide(BigDecimal.valueOf(salesOrdersCount), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        DashboardStatsResponse stats = new DashboardStatsResponse(
                salesOrdersCount,
                totalRevenue,
                todaysSales.size(),
                (int) totalCancelledOrders,
                feedbackService.getAverageRating(),
                averageTicket,
                (int) (todaysOrders.size() - activeCancelledOrders),
                todaysSales.size()
        );

        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
