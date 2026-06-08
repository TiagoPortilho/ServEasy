package com.tiagoportilho.ServEasy.config;

import com.tiagoportilho.ServEasy.model.User;
import com.tiagoportilho.ServEasy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod")
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createDefaultUsers();
        log.info("Default users initialized (dev/docker profile)");
    }

    private void createDefaultUsers() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(User.UserRole.ADMIN);
            admin.setFullName("Administrador do Sistema");
            userRepository.save(admin);
            log.info("User 'admin' created");
        }

        if (!userRepository.existsByUsername("cozinheiro")) {
            User cozinheiro = new User();
            cozinheiro.setUsername("cozinheiro");
            cozinheiro.setPassword(passwordEncoder.encode("cozinha123"));
            cozinheiro.setRole(User.UserRole.COZINHEIRO);
            cozinheiro.setFullName("Cozinheiro Padrão");
            userRepository.save(cozinheiro);
            log.info("User 'cozinheiro' created");
        }

        if (!userRepository.existsByUsername("atendente")) {
            User atendente = new User();
            atendente.setUsername("atendente");
            atendente.setPassword(passwordEncoder.encode("atende123"));
            atendente.setRole(User.UserRole.CLIENTE_ATENDENTE);
            atendente.setFullName("Atendente Padrão");
            userRepository.save(atendente);
            log.info("User 'atendente' created");
        }
    }
}
