package com.tiagoportilho.ServEasy.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {

    @Schema(description = "Total de pedidos de contas fechadas no dia")
    private int totalOrders;

    @Schema(description = "Receita total do dia (apenas contas fechadas)", example = "1250.00")
    private BigDecimal totalRevenue;

    @Schema(description = "Número de clientes atendidos (contas fechadas)", example = "12")
    private int uniqueCustomers;

    @Schema(description = "Total de pedidos cancelados no dia", example = "2")
    private int cancelledOrders;

    @Schema(description = "Avaliação média dos feedbacks (0-5)", example = "4.3")
    private double averageRating;

    @Schema(description = "Ticket médio por pedido fechado", example = "104.17")
    private BigDecimal averageTicket;

    @Schema(description = "Pedidos ainda em aberto (não fechados)", example = "5")
    private int activeOrders;

    @Schema(description = "Número de contas fechadas no dia", example = "12")
    private int closedAccounts;
}
