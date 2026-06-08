package com.tiagoportilho.ServEasy.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:ServEasy}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(applicationName + " — API de Gestão de Restaurante")
                        .description("""
                                API REST do sistema ServEasy para gestão de restaurante.

                                ## Autenticação
                                Clique em **Authorize** e informe o token JWT obtido em `POST /api/auth/login`.
                                Formato: `Bearer <token>`

                                ## Papéis (Roles)
                                - **ADMIN** — acesso total
                                - **COZINHEIRO** — pedidos e cozinha
                                - **CLIENTE_ATENDENTE** — atendimento e mesas

                                ## Respostas
                                Todas as respostas seguem o envelope `ApiResponse<T>`:
                                ```json
                                { "success": true, "message": "...", "data": { } }
                                ```
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Tiago Portilho")
                                .url("https://github.com/TiagoPortilho/ServEasy"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local / Docker")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Token JWT obtido via POST /api/auth/login")));
    }
}
