package com.tiagoportilho.ServEasy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_number", nullable = false)
    private Integer tableNumber;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "items_count", nullable = false)
    private Integer itemsCount;

    @Column(name = "order_ids", columnDefinition = "TEXT")
    private String orderIds; // IDs dos pedidos separados por vírgula

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "cancelled_orders_count")
    private Integer cancelledOrdersCount = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}