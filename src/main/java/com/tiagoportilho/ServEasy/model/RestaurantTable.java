package com.tiagoportilho.ServEasy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tables")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Número da mesa é obrigatório")
    @Column(name = "table_number", unique = true)
    private Integer tableNumber;

    @NotNull(message = "Capacidade é obrigatória")
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private TableStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = TableStatus.DISPONIVEL;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum TableStatus {
        DISPONIVEL, OCUPADA, RESERVADA, MANUTENCAO
    }
}