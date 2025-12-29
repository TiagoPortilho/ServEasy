package com.tiagoportilho.ServEasy.dto.response;

import com.tiagoportilho.ServEasy.model.StockItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de resposta para itens de estoque.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockItemResponse {

    private Long id;
    private String name;
    private String description;
    private Integer quantity;
    private String unit;
    private Integer minQuantity;
    private BigDecimal unitPrice;
    private String supplier;
    private LocalDate entryDate;
    private LocalDate expiryDate;
    private boolean lowStock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static StockItemResponse fromEntity(StockItem stockItem) {
        return StockItemResponse.builder()
                .id(stockItem.getId())
                .name(stockItem.getName())
                .description(stockItem.getDescription())
                .quantity(stockItem.getQuantity())
                .unit(stockItem.getUnit())
                .minQuantity(stockItem.getMinQuantity())
                .unitPrice(stockItem.getUnitPrice())
                .supplier(stockItem.getSupplier())
                .entryDate(stockItem.getEntryDate())
                .expiryDate(stockItem.getExpiryDate())
                .lowStock(stockItem.getQuantity() != null && 
                         stockItem.getMinQuantity() != null && 
                         stockItem.getQuantity() <= stockItem.getMinQuantity())
                .createdAt(stockItem.getCreatedAt())
                .updatedAt(stockItem.getUpdatedAt())
                .build();
    }
}
