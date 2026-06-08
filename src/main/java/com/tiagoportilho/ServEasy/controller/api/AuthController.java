package com.tiagoportilho.ServEasy.controller.api;

import com.tiagoportilho.ServEasy.dto.ApiResponse;
import com.tiagoportilho.ServEasy.dto.LoginRequest;
import com.tiagoportilho.ServEasy.dto.LoginResponse;
import com.tiagoportilho.ServEasy.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Login e geração de token JWT")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login",
            description = "Autentica o usuário e retorna um token JWT. " +
                    "Use o token no header Authorization: Bearer <token> para acessar endpoints protegidos.")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.authenticate(loginRequest);
        if (response.isSuccess()) {
            return ResponseEntity.ok(ApiResponse.success("Login realizado com sucesso", response));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error(response.getMessage(), response));
    }
}
