package com.tiagoportilho.ServEasy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "stock_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do item é obrigatório")
    private String name;

    private String description;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 0, message = "Quantidade não pode ser negativa")
    private Integer quantity;

    @NotNull(message = "Quantidade mínima é obrigatória")
    @Min(value = 0, message = "Quantidade mínima não pode ser negativa")
    @Column(name = "min_quantity")
    private Integer minQuantity;

    @Column(name = "unit_price")
    private java.math.BigDecimal unitPrice; // Preço unitário

    private String unit; // kg, L, unidades, etc.

    @Column(name = "supplier")
    private String supplier;

    @Column(name = "entry_date")
    private LocalDate entryDate; // Data de entrada do produto

    @Column(name = "expiry_date")
    private LocalDate expiryDate; // Data de validade do produto

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public boolean isLowStock() {
        return quantity <= minQuantity;
    }

    public java.math.BigDecimal getTotalValue() {
        if (unitPrice != null && quantity != null) {
            return unitPrice.multiply(new java.math.BigDecimal(quantity));
        }
        return java.math.BigDecimal.ZERO;
    }
}