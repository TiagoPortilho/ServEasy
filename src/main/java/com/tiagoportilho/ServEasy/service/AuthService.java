package com.tiagoportilho.ServEasy.service;

import com.tiagoportilho.ServEasy.dto.LoginRequest;
import com.tiagoportilho.ServEasy.dto.LoginResponse;
import com.tiagoportilho.ServEasy.model.User;
import com.tiagoportilho.ServEasy.repository.UserRepository;
import com.tiagoportilho.ServEasy.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse authenticate(LoginRequest loginRequest) {
        try {
            // Autenticar usando Spring Security
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            // Buscar usuário para obter informações adicionais
            User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Usuário não encontrado"));

            String redirectUrl = getRedirectUrlByRole(user.getRole());
            
            log.info("Login bem-sucedido para usuário: {}", loginRequest.getUsername());
            
            return new LoginResponse(true, "Login realizado com sucesso!", redirectUrl, 
                                   user.getRole(), user.getUsername(), jwt);
                                   
        } catch (BadCredentialsException e) {
            log.warn("Tentativa de login falhada para usuário: {}", loginRequest.getUsername());
            return new LoginResponse(false, "Credenciais inválidas. Tente novamente.", 
                                   null, null, null, null);
        } catch (Exception e) {
            log.error("Erro durante autenticação para usuário: {}", loginRequest.getUsername(), e);
            return new LoginResponse(false, "Erro interno. Tente novamente mais tarde.", 
                                   null, null, null, null);
        }
    }

    private String getRedirectUrlByRole(User.UserRole role) {
        return switch (role) {
            case ADMIN -> "/admin/dashboard";
            case COZINHEIRO -> "/cozinheiro/novos-pedidos";
            case CLIENTE_ATENDENTE -> "/cliente-atendente/cardapio";
        };
    }

    public User createUser(User user) {
        // Validar se username já existe
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username já existe: " + user.getUsername());
        }
        
        // Criptografar senha
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        log.info("Criando novo usuário: {}", user.getUsername());
        return userRepository.save(user);
    }
    
    /**
     * Migra senhas existentes para BCrypt (executar apenas uma vez)
     */
    public void migratePasswordsToHash() {
        userRepository.findAll().forEach(user -> {
            if (!user.getPassword().startsWith("$2a$")) { // Não é BCrypt
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
                userRepository.save(user);
                log.info("Senha migrada para usuário: {}", user.getUsername());
            }
        });
    }
}