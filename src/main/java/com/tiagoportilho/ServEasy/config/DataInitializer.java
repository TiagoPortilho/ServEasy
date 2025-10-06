package com.tiagoportilho.ServEasy.config;

import com.tiagoportilho.ServEasy.model.User;
import com.tiagoportilho.ServEasy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Criar usuários padrão se não existirem
        createDefaultUsers();
        
        System.out.println("=== SISTEMA INICIADO COM USUÁRIOS PADRÃO ===");
        System.out.println("Admin: admin / admin123");
        System.out.println("Cozinheiro: cozinheiro / cozinha123");
        System.out.println("Atendente: atendente / atende123");
        System.out.println("=============================================");
    }
    
    private void createDefaultUsers() {
        // Criar usuário Admin
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setRole(User.UserRole.ADMIN);
            admin.setFullName("Administrador do Sistema");
            userRepository.save(admin);
            System.out.println("✓ Usuário admin criado com sucesso!");
        }
        
        // Criar usuário Cozinheiro
        if (!userRepository.existsByUsername("cozinheiro")) {
            User cozinheiro = new User();
            cozinheiro.setUsername("cozinheiro");
            cozinheiro.setPassword("cozinha123");
            cozinheiro.setRole(User.UserRole.COZINHEIRO);
            cozinheiro.setFullName("Cozinheiro Padrão");
            userRepository.save(cozinheiro);
            System.out.println("✓ Usuário cozinheiro criado com sucesso!");
        }
        
        // Criar usuário Atendente
        if (!userRepository.existsByUsername("atendente")) {
            User atendente = new User();
            atendente.setUsername("atendente");
            atendente.setPassword("atende123");
            atendente.setRole(User.UserRole.CLIENTE_ATENDENTE);
            atendente.setFullName("Atendente Padrão");
            userRepository.save(atendente);
            System.out.println("✓ Usuário atendente criado com sucesso!");
        }
    }
}