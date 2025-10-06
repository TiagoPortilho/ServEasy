package com.tiagoportilho.ServEasy.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // Este arquivo foi deixado intencionalmente vazio, ele serve para inicializar dados se necessário no futuro.
        // Sistema iniciará com banco de dados limpo
        // Todos os dados devem ser inseridos manualmente através da interface
        System.out.println("Sistema iniciado com banco de dados limpo - sem dados pré-populados");
    }
}