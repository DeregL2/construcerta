package com.construcerta.controller;

import com.construcerta.dto.ProdutoForm;
import com.construcerta.security.CustomUserDetails;
import com.construcerta.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class VendedorController {
    private final ProdutoService produtoService;

    public VendedorController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/vendedor/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails principal, Model model) {
        model.addAttribute("usuario", principal.getDomainUser());
        model.addAttribute("produtos", produtoService.listarTodos());
        if (!model.containsAttribute("produtoForm")) {
            model.addAttribute("produtoForm", new ProdutoForm());
        }
        return "vendedor/dashboard";
    }

    @PostMapping("/vendedor/produtos/salvar")
    public String salvarProduto(@Valid @ModelAttribute("produtoForm") ProdutoForm form,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("produtos", produtoService.listarTodos());
            return "vendedor/dashboard";
        }

        produtoService.salvar(form);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Produto salvo com sucesso.");
        return "redirect:/vendedor/dashboard";
    }
}
