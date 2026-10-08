package com.construcerta.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.theme")
public class ThemeProperties {
    private String name = "ConstruCerta";
    private String primaryColor = "#d9641e";
    private String secondaryColor = "#f2994a";
    private String accentColor = "#2f6f4f";
    private String backgroundColor = "#fbf4ec";
    private String textColor = "#2e2015";
    private String logoText = "ConstruCerta";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getAccentColor() {
        return accentColor;
    }

    public void setAccentColor(String accentColor) {
        this.accentColor = accentColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getLogoText() {
        return logoText;
    }

    public void setLogoText(String logoText) {
        this.logoText = logoText;
    }
}
