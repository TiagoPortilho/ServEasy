package com.tiagoportilho.ServEasy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser pelo menos 1")
    private Integer quantity;

    @NotNull(message = "Preço unitário é obrigatório")
    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @NotNull(message = "Subtotal é obrigatório")
    private BigDecimal subtotal;

    private String notes;

    @PrePersist
    @PreUpdate
    private void calculateSubtotal() {
        if (unitPrice != null && quantity != null) {
            subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}