package com.tiagoportilho.ServEasy.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FeedbackDto {
    @NotBlank(message = "Nome do cliente é obrigatório")
    private String customerName;

    @NotNull(message = "Avaliação é obrigatória")
    @Min(value = 1, message = "Avaliação deve ser entre 1 e 5")
    @Max(value = 5, message = "Avaliação deve ser entre 1 e 5")
    private Integer rating;

    @NotBlank(message = "Comentário é obrigatório")
    private String comment;

    private Long orderId; // Opcional
    private Integer tableNumber; // Opcional
}