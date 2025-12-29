package com.tiagoportilho.ServEasy.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para criação de itens de estoque.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockItemRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    private String description;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 0, message = "Quantidade não pode ser negativa")
    private Integer quantity;

    @NotBlank(message = "Unidade de medida é obrigatória")
    private String unit;

    @Min(value = 0, message = "Quantidade mínima não pode ser negativa")
    private Integer minimumQuantity;
}
