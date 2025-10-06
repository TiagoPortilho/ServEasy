package com.tiagoportilho.ServEasy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDto {
    private Long stockItemId;
    private String stockItemName;
    private BigDecimal quantity;
    private String unit;
    private Boolean showUnit = true; // Controla se a unidade deve ser exibida
}