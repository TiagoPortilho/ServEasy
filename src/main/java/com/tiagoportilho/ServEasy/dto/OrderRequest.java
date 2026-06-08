package com.tiagoportilho.ServEasy.dto;

import com.tiagoportilho.ServEasy.dto.request.OrderItemRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @Schema(description = "Número da mesa", example = "5", minimum = "1")
    @NotNull(message = "Número da mesa é obrigatório")
    @Min(value = 1, message = "Número da mesa deve ser maior que zero")
    private Integer tableNumber;

    @Schema(description = "Nome do cliente (opcional)", example = "João Silva")
    @Size(max = 100, message = "Nome do cliente deve ter no máximo 100 caracteres")
    private String customerName;

    @Schema(description = "Observações gerais do pedido", example = "Mesa perto da janela")
    private String observations;

    @Schema(description = "Itens do pedido. Deve conter pelo menos um item.")
    @NotEmpty(message = "O pedido deve ter pelo menos um item")
    @Valid
    private List<OrderItemRequest> items;
}
