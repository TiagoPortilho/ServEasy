package com.tiagoportilho.ServEasy.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @Schema(description = "Nome de usuário", example = "admin")
    @NotBlank(message = "Username é obrigatório")
    private String username;

    @Schema(description = "Senha do usuário", example = "admin123")
    @NotBlank(message = "Senha é obrigatória")
    private String password;
}
