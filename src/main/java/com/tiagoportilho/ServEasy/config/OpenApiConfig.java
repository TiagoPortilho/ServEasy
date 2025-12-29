package com.tiagoportilho.ServEasy.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração do Swagger/OpenAPI para documentação da API.
 * Acesse: http://localhost:8080/swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:ServEasy}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(applicationName + " - API de Gestão de Restaurante")
                        .description("""
                                API REST para o sistema de gestão de restaurante ServEasy.
                                
                                ## Funcionalidades
                                - **Autenticação**: Login e gerenciamento de sessão
                                - **Pedidos**: Criação, atualização e acompanhamento de pedidos
                                - **Cardápio**: Gerenciamento de itens do menu
                                - **Estoque**: Controle de ingredientes e produtos
                                - **Mesas**: Gerenciamento de mesas do restaurante
                                - **Feedbacks**: Avaliações dos clientes
                                - **Dashboard**: Estatísticas e métricas
                                
                                ## Códigos de Status
                                - `200 OK`: Requisição bem-sucedida
                                - `201 Created`: Recurso criado com sucesso
                                - `400 Bad Request`: Erro de validação ou dados inválidos
                                - `404 Not Found`: Recurso não encontrado
                                - `500 Internal Server Error`: Erro interno do servidor
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Tiago Portilho")
                                .email("contato@serveasy.com")
                                .url("https://github.com/TiagoPortilho/ServEasy"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento"),
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor Docker")))
                .tags(List.of(
                        new Tag().name("Autenticação").description("Endpoints de login e autenticação"),
                        new Tag().name("Pedidos").description("Gerenciamento de pedidos"),
                        new Tag().name("Cardápio").description("Gerenciamento do cardápio"),
                        new Tag().name("Estoque").description("Controle de estoque"),
                        new Tag().name("Mesas").description("Gerenciamento de mesas"),
                        new Tag().name("Feedbacks").description("Avaliações dos clientes"),
                        new Tag().name("Dashboard").description("Estatísticas e métricas")));
    }
}
