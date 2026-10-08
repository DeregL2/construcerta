package com.construcerta.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {
    private final ThemeProperties themeProperties;

    public GlobalModelAttributes(ThemeProperties themeProperties) {
        this.themeProperties = themeProperties;
    }

    @ModelAttribute("theme")
    public ThemeProperties theme() {
        return themeProperties;
    }
}
