package com.construcerta.controller;

import com.construcerta.model.Role;
import com.construcerta.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal CustomUserDetails principal) {
        var user = principal.getDomainUser();
        if (user.hasRole(Role.ADMIN)) {
            return "redirect:/admin/dashboard";
        }
        if (user.hasRole(Role.VENDEDOR)) {
            return "redirect:/vendedor/dashboard";
        }
        return "redirect:/cliente/dashboard";
    }

    @GetMapping("/403")
    public String acessoNegado() {
        return "error/403";
    }
}
