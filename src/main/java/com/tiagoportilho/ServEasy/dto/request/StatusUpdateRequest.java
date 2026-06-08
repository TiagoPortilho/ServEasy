package com.tiagoportilho.ServEasy.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateRequest {

    @Schema(description = "Novo status do pedido", example = "EM_ANDAMENTO",
            allowableValues = {"NOVO", "EM_ANDAMENTO", "PRONTO", "ENTREGUE", "CANCELADO"})
    @NotBlank(message = "Status é obrigatório")
    private String status;
}
