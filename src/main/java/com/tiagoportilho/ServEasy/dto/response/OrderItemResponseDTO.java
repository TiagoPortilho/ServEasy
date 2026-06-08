package com.tiagoportilho.ServEasy.dto.response;

import com.tiagoportilho.ServEasy.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDTO {
    private Long id;
    private MenuItemSimpleDTO menuItem;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private String notes;

    public static OrderItemResponseDTO fromEntity(OrderItem orderItem) {
        OrderItemResponseDTO dto = new OrderItemResponseDTO();
        dto.setId(orderItem.getId());
        dto.setQuantity(orderItem.getQuantity());
        dto.setUnitPrice(orderItem.getUnitPrice());
        dto.setSubtotal(orderItem.getSubtotal());
        dto.setNotes(orderItem.getNotes());
        if (orderItem.getMenuItem() != null) {
            dto.setMenuItem(MenuItemSimpleDTO.fromEntity(orderItem.getMenuItem()));
        }
        return dto;
    }
}
