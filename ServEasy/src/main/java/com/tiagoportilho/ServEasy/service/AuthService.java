package com.tiagoportilho.ServEasy.service;

import com.tiagoportilho.ServEasy.dto.LoginRequest;
import com.tiagoportilho.ServEasy.dto.LoginResponse;
import com.tiagoportilho.ServEasy.model.User;
import com.tiagoportilho.ServEasy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;

    public LoginResponse authenticate(LoginRequest loginRequest) {
        // Validação básica para o usuário admin padrão
        if ("admin".equals(loginRequest.getUsername()) && "admin".equals(loginRequest.getPassword())) {
            return new LoginResponse(true, "Login realizado com sucesso!", "/admin/dashboard", User.UserRole.ADMIN, "admin");
        }

        // Buscar usuário no banco de dados
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Em um sistema real, você usaria BCrypt ou similar para verificar a senha
            if (user.getPassword().equals(loginRequest.getPassword())) {
                String redirectUrl = getRedirectUrlByRole(user.getRole());
                return new LoginResponse(true, "Login realizado com sucesso!", redirectUrl, user.getRole(), user.getUsername());
            }
        }
        
        return new LoginResponse(false, "Credenciais inválidas. Tente novamente.", null, null, null);
    }

    private String getRedirectUrlByRole(User.UserRole role) {
        return switch (role) {
            case ADMIN -> "/admin/dashboard";
            case COZINHEIRO -> "/cozinheiro/novos-pedidos";
            case CLIENTE_ATENDENTE -> "/cliente-atendente/cardapio";
        };
    }

    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username já existe");
        }
        return userRepository.save(user);
    }
}