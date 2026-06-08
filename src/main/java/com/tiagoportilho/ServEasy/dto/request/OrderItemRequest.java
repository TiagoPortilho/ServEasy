package com.tiagoportilho.ServEasy.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {

    @Schema(description = "ID do item no cardápio", example = "3")
    @NotNull(message = "ID do item é obrigatório")
    private Long menuItemId;

    @Schema(description = "Quantidade do item", example = "2", minimum = "1")
    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser maior que zero")
    private Integer quantity;

    @Schema(description = "Observações sobre o item (ex: sem cebola)", example = "Sem cebola")
    private String notes;
}
