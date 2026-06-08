package com.tiagoportilho.ServEasy.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiagoportilho.ServEasy.dto.request.LoginRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("ServEasy Integration Tests")
class ServEasyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Spring context starts successfully")
    void context_loads() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    @DisplayName("POST /api/auth/login retorna JWT para credenciais válidas")
    void login_with_valid_credentials_returns_jwt() throws Exception {
        LoginRequest request = new LoginRequest("admin", "admin123");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value("admin"))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).contains("token");
    }

    @Test
    @DisplayName("POST /api/auth/login rejeita credenciais inválidas")
    void login_with_invalid_credentials_returns_error() throws Exception {
        LoginRequest request = new LoginRequest("admin", "senhaerrada");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/login retorna 400 para body sem username")
    void login_with_blank_username_returns_400() throws Exception {
        LoginRequest request = new LoginRequest("", "admin123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/menu retorna 200 sem autenticação")
    void menu_endpoint_is_public() throws Exception {
        mockMvc.perform(get("/api/menu"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/orders retorna 401 sem autenticação")
    void orders_endpoint_requires_authentication() throws Exception {
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/dashboard retorna 401 sem autenticação")
    void dashboard_endpoint_requires_authentication() throws Exception {
        mockMvc.perform(get("/api/dashboard"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/orders retorna 200 com JWT de admin")
    void orders_endpoint_returns_ok_with_valid_jwt() throws Exception {
        String token = obtainAdminToken();

        mockMvc.perform(get("/api/orders")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    private String obtainAdminToken() throws Exception {
        LoginRequest request = new LoginRequest("admin", "admin123");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        String body = result.getResponse().getContentAsString();
        return objectMapper.readTree(body).path("data").path("token").asText();
    }
}
