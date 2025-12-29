package com.tiagoportilho.ServEasy.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para criação/atualização de mesas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableRequest {

    @NotNull(message = "Número da mesa é obrigatório")
    @Positive(message = "Número da mesa deve ser positivo")
    private Integer tableNumber;

    @Positive(message = "Capacidade deve ser positiva")
    private Integer capacity;

    private String location;

    @Builder.Default
    private Boolean isAvailable = true;
}
