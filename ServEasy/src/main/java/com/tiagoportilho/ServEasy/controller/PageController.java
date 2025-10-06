package com.tiagoportilho.ServEasy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Admin pages
    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/admin/menu")
    public String adminMenu() {
        return "admin/menu";
    }

    @GetMapping("/admin/tables")
    public String adminTables() {
        return "admin/tables";
    }

    @GetMapping("/admin/stock")
    public String adminStock() {
        return "admin/stock";
    }

    @GetMapping("/admin/feedbacks")
    public String adminFeedbacks() {
        return "admin/feedbacks";
    }

    // Cozinheiro pages
    @GetMapping("/cozinheiro/novos-pedidos")
    public String cozinheiroNovosPedidos() {
        return "cozinheiro/novos-pedidos";
    }

    @GetMapping("/cozinheiro/pedidos-andamento")
    public String cozinheiroPedidosAndamento() {
        return "cozinheiro/pedidos-andamento";
    }

    @GetMapping("/cozinheiro/pedidos-prontos")
    public String cozinheiroPedidosProntos() {
        return "cozinheiro/pedidos-prontos";
    }

    // Cliente-Atendente pages
    @GetMapping("/cliente-atendente/cardapio")
    public String clienteAtendenteCardapio() {
        return "cliente-atendente/cardapio";
    }

    @GetMapping("/cliente-atendente/seus-pedidos")
    public String clienteAtendenteSeusPedidos() {
        return "cliente-atendente/seus-pedidos";
    }

    @GetMapping("/cliente-atendente/feedback")
    public String clienteAtendenteFeedback() {
        return "cliente-atendente/feedback";
    }
}