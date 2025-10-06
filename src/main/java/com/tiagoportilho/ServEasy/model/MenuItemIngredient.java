package com.tiagoportilho.ServEasy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "menu_item_ingredients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    @JsonBackReference
    private MenuItem menuItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_item_id", nullable = false)
    @JsonManagedReference
    private StockItem stockItem;

    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    @Column(name = "quantity", nullable = false)
    private BigDecimal quantity;

    @Column(name = "unit")
    private String unit; // Unidade específica para esta receita (pode ser diferente do estoque)

    @Column(name = "show_unit", nullable = false)
    private Boolean showUnit = true; // Controla se a unidade deve ser exibida para este ingrediente
}