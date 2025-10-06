package com.tiagoportilho.ServEasy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Integer tableNumber;
    private String customerName;
    private String observations; // Mudança de notes para observations
    private String status;
    private List<OrderItemRequest> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequest {
        private Long menuItemId;
        private Integer quantity;
        private BigDecimal unitPrice; // Adicionado unitPrice
        private String notes;
    }
}