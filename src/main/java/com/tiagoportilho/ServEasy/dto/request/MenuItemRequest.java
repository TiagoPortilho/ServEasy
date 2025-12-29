package com.tiagoportilho.ServEasy.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para criação/atualização de itens do cardápio.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    private String description;

    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser positivo")
    private BigDecimal price;

    @NotBlank(message = "Categoria é obrigatória")
    private String category;

    private String imageUrl;

    @Builder.Default
    private Boolean isAvailable = true;

    @Min(value = 0, message = "Tempo de preparo não pode ser negativo")
    private Integer preparationTime;

    private List<IngredientRequest> ingredients;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IngredientRequest {
        @NotNull(message = "ID do item de estoque é obrigatório")
        private Long stockItemId;

        @NotNull(message = "Quantidade é obrigatória")
        @Positive(message = "Quantidade deve ser positiva")
        private Double quantity;
    }
}
