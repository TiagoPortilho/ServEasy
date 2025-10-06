package com.tiagoportilho.ServEasy.controller.api;

import com.tiagoportilho.ServEasy.dto.ApiResponse;
import com.tiagoportilho.ServEasy.dto.LoginRequest;
import com.tiagoportilho.ServEasy.dto.LoginResponse;
import com.tiagoportilho.ServEasy.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = authService.authenticate(loginRequest);
            if (response.isSuccess()) {
                return ResponseEntity.ok(ApiResponse.success("Login realizado com sucesso", response));
            } else {
                return ResponseEntity.badRequest().body(ApiResponse.error(response.getMessage(), response));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Erro interno do servidor: " + e.getMessage()));
        }
    }
}