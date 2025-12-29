package com.tiagoportilho.ServEasy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de resposta para estatísticas do dashboard.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {

    private int totalOrders;
    private int pendingOrders;
    private int completedOrders;
    private int cancelledOrders;
    private BigDecimal totalRevenue;
    private BigDecimal averageTicket;
    private double averageRating;
    private int totalFeedbacks;
    private int lowStockItems;
    private int availableTables;
    private int occupiedTables;
    private LocalDateTime generatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrdersByStatus {
        private int novo;
        private int emAndamento;
        private int pronto;
        private int entregue;
        private int cancelado;
    }
}
