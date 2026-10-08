package com.construcerta.controller;

import com.construcerta.security.CustomUserDetails;
import com.construcerta.service.ProdutoService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClienteController {
    private final ProdutoService produtoService;

    public ClienteController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/cliente/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails principal, Model model) {
        model.addAttribute("usuario", principal.getDomainUser());
        model.addAttribute("produtos", produtoService.listarAtivos());
        return "cliente/dashboard";
    }
}
