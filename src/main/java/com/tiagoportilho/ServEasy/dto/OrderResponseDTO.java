package com.tiagoportilho.ServEasy.dto;

import com.tiagoportilho.ServEasy.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private Integer tableNumber;
    private String customerName;
    private BigDecimal total;
    private String status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemResponseDTO> items;

    public static OrderResponseDTO fromEntity(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setTableNumber(order.getTable() != null ? order.getTable().getTableNumber() : null);
        dto.setCustomerName(order.getCustomerName());
        dto.setTotal(order.getTotal());
        dto.setStatus(order.getStatus() != null ? order.getStatus().name() : null);
        dto.setNotes(order.getNotes());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        
        if (order.getItems() != null) {
            dto.setItems(order.getItems().stream()
                .map(OrderItemResponseDTO::fromEntity)
                .collect(Collectors.toList()));
        }
        
        return dto;
    }
}